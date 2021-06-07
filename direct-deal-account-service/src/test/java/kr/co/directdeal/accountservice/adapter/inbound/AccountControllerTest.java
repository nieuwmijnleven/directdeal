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

import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.service.AccountService;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;
import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.common.security.util.SecurityUtils;

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
    @WithMockUser(username = "account@directdeal.co.kr")
    public void CreateAccount_UniqueEmail_Created() throws Exception {
        //given
        given(accountService.createAccount(any(AccountDTO.class)))
            .willReturn(any(AccountDTO.class));
        
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
                    //.andDo(print())
                    .andExpect(status().isCreated());                   
    }
    
    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void CreateAccount_DuplicateEmail_ThrowAccountException() throws Exception {
        //given
        String incorrectEmail = "incorrect@directdeal.co.kr"; 

        willThrow(AccountException.builder()
                    .messageKey("account.exception.insert.email.message")
                    .messageArgs(new String[]{incorrectEmail})
                    .build())
            .given(accountService).createAccount(any(AccountDTO.class));
        
        AccountDTO accountDTO = AccountDTO.builder()
                                    .email(incorrectEmail)
                                    .password("1q2w3e")
                                    .name("account")
                                    .activated(true)
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
                    .andExpect(jsonPath("$.message", is("The email(incorrect@directdeal.co.kr) is already used")));

    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void UpdateAccount_CorrectAccountId_Updated() throws Exception {
        //given
        String loginEmail = SecurityUtils.getCurrentUserLogin();

        AccountDTO accountDTO = AccountDTO.builder()
                                    .id("1")
                                    .email(loginEmail)
                                    .password("1q2w3e")
                                    .name("account")
                                    .build();
        
        given(accountService.updateAccount(accountDTO))
            .willReturn(mock(AccountDTO.class));

        String payload = objectMapper.writeValueAsString(accountDTO);
        
        //when and then
        this.mvc.perform(put("/account")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            //.andDo(print())
            .andExpect(status().isCreated()); 
    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void UpdateAccount_InCorrectAccountEmail_ThrowAccountException() throws Exception {
        //given
        String incorrectEmail = "incorrect@directdeal.co.kr"; 

        willThrow(AccountException.builder()
                    .messageKey("account.exception.update.id.message")
                    .messageArgs(new String[]{incorrectEmail})
                    .build())
            .given(accountService).updateAccount(any(AccountDTO.class));

        AccountDTO accountDTO = AccountDTO.builder()
                                    .id("1")
                                    .email("incorrect@directdeal.co.kr")
                                    .password("1q2w3e")
                                    .name("account")
                                    .build();

        String payload = objectMapper.writeValueAsString(accountDTO);
        
        //when and then
        this.mvc.perform(put("/account")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Failed to update the account(incorrect@directdeal.co.kr)")));
    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void GetAccount_CorrectAccountId_ReturnAccountDTO() throws Exception {
        //given
        String loginEmail = SecurityUtils.getCurrentUserLogin();

        AccountDTO resultDTO = AccountDTO.builder()
                                    .id("1")
                                    .email(loginEmail)
                                    .name("account")
                                    .activated(true)
                                    .build();

        given(accountService.getAccount(loginEmail))
            .willReturn(resultDTO);
        
        //when and then
        this.mvc.perform(get("/account")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(resultDTO.getId())))
            .andExpect(jsonPath("$.email", is(resultDTO.getEmail())))
            .andExpect(jsonPath("$.name", is(resultDTO.getName())));
    }
    
    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void GetAccount_InCorrectAccountEmail_ThrowAccountException() throws Exception {
        //given
        String incorrectEmail = "incorrect@directdeal.co.kr"; 
        String loginEmail = SecurityUtils.getCurrentUserLogin();

        willThrow(AccountException.builder()
                    .messageKey("account.exception.get.message")
                    .messageArgs(new String[]{incorrectEmail})
                    .build())
            .given(accountService).getAccount(loginEmail);
        
        //when and then
        this.mvc.perform(get("/account")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Failed to find the account(incorrect@directdeal.co.kr)")));
    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void DeleteAccount_CorrectAccountEmail_Deleted() throws Exception {
        //given
        
        //when and then
        this.mvc.perform(delete("/account")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void DeleteAccount_InCorrectAccountEmail_ThrowAccountException() throws Exception {
        //given
        String incorrectEmail = "incorrect@directdeal.co.kr"; 
        String loginEmail = SecurityUtils.getCurrentUserLogin();
        willThrow(AccountException.builder()
                    .messageKey("account.exception.delete.message")
                    .messageArgs(new String[]{incorrectEmail})
                    .build())
            .given(accountService).deleteAccount(loginEmail);
        
        //when and then
        this.mvc.perform(delete("/account")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Failed to delete the account(incorrect@directdeal.co.kr)")));
    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void ChangePassword_CorrectPasswordDTO_Changed() throws Exception {
        //given
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                    .password("1q2w3e")
                                    .newPassword("123qwe")
                                    .build();

        String payload = objectMapper.writeValueAsString(passwordDTO);
          
        //when and then
        this.mvc.perform(put("/account/change-password")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "account@directdeal.co.kr")
    public void ChangePassword_IncorrectPasswordDTO_ThrowAccountException() throws Exception {
        //given
        String loginEmail = SecurityUtils.getCurrentUserLogin();
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                    .password("1q2w3e")
                                    .newPassword("123qwe")
                                    .build();

        willThrow(AccountException.builder()
                    .messageKey("account.exception.changepassword.passwordmismatch.message")
                    .messageArgs(new String[]{loginEmail})
                    .build())
            .given(accountService).changePassword(loginEmail, passwordDTO);

        String payload = objectMapper.writeValueAsString(passwordDTO);
        
        //when and then
        this.mvc.perform(put("/account/change-password")
            .content(payload)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error", is("Account Error")))
            .andExpect(jsonPath("$.message", is("Incorrect password")));
    }
}
