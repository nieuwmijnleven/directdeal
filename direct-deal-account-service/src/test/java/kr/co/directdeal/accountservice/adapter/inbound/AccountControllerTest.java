package kr.co.directdeal.accountservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.accountservice.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.accountservice.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.accountservice.auth.jwt.TokenProvider;
import kr.co.directdeal.accountservice.config.props.JWTProperties;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.service.AccountService;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AccountController.class}, 
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class, 
                   JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class}))
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser("USER")
    public void CreateAccount_UniqueEmail_Created() throws Exception {
        //given
        AccountDTO resultDTO = AccountDTO.builder()
                                    .id("1")
                                    .build();

        given(accountService.createAccount(any(AccountDTO.class)))
            .willReturn(resultDTO);
        
        AccountDTO accountDTO = AccountDTO.builder()
                                    .email("account@directdeal.co.kr")
                                    .password("1q2w3e")
                                    .name("account")
                                    .build();

        String payload = objectMapper.writeValueAsString(accountDTO);

        //when and then
        this.mvc.perform(post("/account")
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/account/1"));                   
    }
    
    @Test
    @WithMockUser("USER")
    public void CreateAccount_DuplicateEmail_ThrowAccountException() throws Exception {
        //given
        willThrow(AccountException.builder()
                    .messageKey("account.exception.insert.email.message")
                    .messageArgs(new String[]{"account@directdeal.co.kr"})
                    .build())
            .given(accountService).createAccount(any(AccountDTO.class));
        
        AccountDTO accountDTO = AccountDTO.builder()
                                    .email("account@directdeal.co.kr")
                                    .password("1q2w3e")
                                    .name("account")
                                    .build();

        String payload = objectMapper.writeValueAsString(accountDTO);

        //when and then
        this.mvc.perform(post("/account")
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Account Error")))
                    .andExpect(jsonPath("$.message", is("The email(account@directdeal.co.kr) is already used")));

    }

    @Test
    @WithMockUser("USER")
    public void UpdateAccount_CorrectAccountId_Updated() throws Exception {
        //given
        given(accountService.updateAccount(any(AccountDTO.class)))
            .willReturn(mock(AccountDTO.class));

        AccountDTO accountDTO = AccountDTO.builder()
                                    .id("1")
                                    .email("account@directdeal.co.kr")
                                    .password("1q2w3e")
                                    .name("account")
                                    .build();

        String payload = objectMapper.writeValueAsString(accountDTO);
        
        //when and then
        this.mvc.perform(put("/account/1")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            //.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/account/1"));  
    }

    @Test
    @WithMockUser("USER")
    public void UpdateAccount_InCorrectAccountId_ThrowAccountException() throws Exception {
        //given
        willThrow(AccountException.builder()
                    .messageKey("account.exception.update.id.message")
                    .messageArgs(new String[]{"1"})
                    .build())
            .given(accountService).updateAccount(any(AccountDTO.class));

        AccountDTO accountDTO = AccountDTO.builder()
                                    .id("1")
                                    .email("account@directdeal.co.kr")
                                    .password("1q2w3e")
                                    .name("account")
                                    .build();

        String payload = objectMapper.writeValueAsString(accountDTO);
        
        //when and then
        this.mvc.perform(put("/account/1")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Failed to update the account(1)")));
    }

    @Test
    @WithMockUser("USER")
    public void GetAccount_CorrectAccountId_ThrowAccountException() throws Exception {
        //given
        String accountId = "1";
        AccountDTO resultDTO = AccountDTO.builder()
                                    .id(accountId)
                                    .email("account@directdeal.co.kr")
                                    .name("account")
                                    .activated(true)
                                    .build();

        given(accountService.getAccount(accountId))
            .willReturn(resultDTO);
        
        //when and then
        this.mvc.perform(get("/account/" + accountId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(resultDTO.getId())))
            .andExpect(jsonPath("$.email", is(resultDTO.getEmail())))
            .andExpect(jsonPath("$.name", is(resultDTO.getName())));
    }
    
    @Test
    @WithMockUser("USER")
    public void GetAccount_InCorrectAccountId_ReturnAccountDTO() throws Exception {
        //given
        String accountId = "1";
        willThrow(AccountException.builder()
                    .messageKey("account.exception.get.message")
                    .messageArgs(new String[]{accountId})
                    .build())
            .given(accountService).getAccount(accountId);
        
        //when and then
        this.mvc.perform(get("/account/" + accountId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Failed to find the account(1)")));
    }

    @Test
    @WithMockUser("USER")
    public void DeleteAccount_CorrectAccountId_Deleted() throws Exception {
        //given
        String accountId = "1";
        
        //when and then
        this.mvc.perform(delete("/account/" + accountId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("USER")
    public void DeleteAccount_InCorrectAccountId_ThrowAccountException() throws Exception {
        //given
        String accountId = "1";
        willThrow(AccountException.builder()
                    .messageKey("account.exception.delete.message")
                    .messageArgs(new String[]{accountId})
                    .build())
            .given(accountService).deleteAccount(accountId);
        
        //when and then
        this.mvc.perform(delete("/account/" + accountId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Failed to delete the account(1)")));
    }

    @Test
    @WithMockUser("USER")
    public void ChangePassword_CorrectPasswordDTO_Changed() throws Exception {
        //given
        String accountId = "1";
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                    .id(accountId)
                                    .password("1q2w3e")
                                    .newPassword("123qwe")
                                    .build();

        String payload = objectMapper.writeValueAsString(passwordDTO);
        
        //when and then
        this.mvc.perform(put("/account/" + accountId + "/change-password")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser("USER")
    public void ChangePassword_IncorrectPasswordDTO_ThrowAccountException() throws Exception {
        //given
        String accountId = "1";
        willThrow(AccountException.builder()
                    .messageKey("account.exception.changepassword.passwordmismatch.message")
                    .messageArgs(new String[]{accountId})
                    .build())
            .given(accountService).changePassword(any(PasswordDTO.class));

        PasswordDTO passwordDTO = PasswordDTO.builder()
                                    .id(accountId)
                                    .password("1q2w3e")
                                    .newPassword("123qwe")
                                    .build();

        String payload = objectMapper.writeValueAsString(passwordDTO);
        
        //when and then
        this.mvc.perform(put("/account/" + accountId + "/change-password")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Incorrect password")));
    }
}
