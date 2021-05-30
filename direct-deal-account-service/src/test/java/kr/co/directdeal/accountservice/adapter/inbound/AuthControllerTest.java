package kr.co.directdeal.accountservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.accountservice.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.accountservice.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.accountservice.auth.jwt.TokenProvider;
import kr.co.directdeal.accountservice.config.props.JWTProperties;
import kr.co.directdeal.accountservice.domain.account.Account;
import kr.co.directdeal.accountservice.domain.account.Authority;
import kr.co.directdeal.accountservice.service.AccountDetailService;
import kr.co.directdeal.accountservice.service.dto.LoginDTO;
import kr.co.directdeal.accountservice.service.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AuthController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class, AccountDetailService.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class}))
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void Login_InvalidEmail_ThrowAccountException() throws Exception {
        //given
        LoginDTO loginDTO = LoginDTO.builder()
                                .email("admin@directdeal.co.kr")
                                .password("1q2w3e")
                                .build();
        
        given(accountRepository.findOneWithAuthoritiesByEmail(loginDTO.getEmail()))
            .willReturn(Optional.empty());

        String payload = objectMapper.writeValueAsString(loginDTO);

        //when and then
        this.mvc.perform(post("/auth/login")
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Authentication Error")))
                    .andExpect(jsonPath("$.message", is("Bad credentials")));

        verify(accountRepository).findOneWithAuthoritiesByEmail(any(String.class));
    }

    @Test
    public void Login_ValidEmailAndPassword_ReturnJWT() throws Exception {
        //given
        LoginDTO loginDTO = LoginDTO.builder()
                                .email("account@directdeal.co.kr")
                                .password("1q2w3e")
                                .build();

        Account accountByEmail = Account.builder()
                                    .id("1")
                                    .email("account@directdeal.co.kr")
                                    .password("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he")
                                    .name("account")
                                    .authorities(Collections.singleton(new Authority("ROLE_USER")))
                                    .activated(true)
                                    .build();

        given(accountRepository.findOneWithAuthoritiesByEmail(loginDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));
                                
        String payload = objectMapper.writeValueAsString(loginDTO);

        //when and then
        this.mvc.perform(post("/auth/login")
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Authorization", matchesPattern("^Bearer [a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(jsonPath("$.token", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")));

        verify(accountRepository).findOneWithAuthoritiesByEmail(loginDTO.getEmail());
    }

}
