package kr.co.directdeal.accountservice.adapter.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.directdeal.accountservice.application.service.AccountDetailService;
import kr.co.directdeal.accountservice.application.service.dto.LoginDTO;
import kr.co.directdeal.accountservice.domain.object.Account;
import kr.co.directdeal.accountservice.domain.object.Authority;
import kr.co.directdeal.accountservice.port.outbound.AccountRepository;
import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AuthController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class, AccountDetailService.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class}))
@Import(AuthControllerTest.TestSecurityConfig.class)
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTProperties jwtProperties;

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
                    .with(csrf())
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Authentication Failed")))
                    .andExpect(jsonPath("$.message", is("Bad credentials")));

        verify(accountRepository).findOneWithAuthoritiesByEmail(any(String.class));
    }

    @Test
    public void Login_ValidEmailAndInvalidPassword_ReturnJWT() throws Exception {
        //given
        LoginDTO loginDTO = LoginDTO.builder()
                                .email("account@directdeal.co.kr")
                                .password("123qwe")
                                .build();

        Account accountByEmail = Account.builder()
                                    .id(1L)
                                    .email("account@directdeal.co.kr")
                                    .password("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he")
                                    .name("account")
                                    .authorities(Collections.singleton(Authority.USER))
                                    .activated(true)
                                    .build();

        given(accountRepository.findOneWithAuthoritiesByEmail(loginDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));
                                
        String payload = objectMapper.writeValueAsString(loginDTO);

        //when and then
        this.mvc.perform(post("/auth/login")
                    .with(csrf())
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Authentication Failed")))
                    .andExpect(jsonPath("$.message", is("Bad credentials")));

        verify(accountRepository).findOneWithAuthoritiesByEmail(loginDTO.getEmail());
    }

    @Test
    public void Login_ValidEmailAndPassword_ReturnJWT() throws Exception {
        //given
        LoginDTO loginDTO = LoginDTO.builder()
                                .email("account@directdeal.co.kr")
                                .password("1q2w3e")
                                .build();

        Account accountByEmail = Account.builder()
                                    .id(1L)
                                    .email("account@directdeal.co.kr")
                                    .password("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he")
                                    .name("account")
                                    .authorities(Collections.singleton(Authority.USER))
                                    .activated(true)
                                    .build();

        given(accountRepository.findOneWithAuthoritiesByEmail(loginDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));
                                
        String payload = objectMapper.writeValueAsString(loginDTO);

        //when and then
        this.mvc.perform(post("/auth/login")
                    .with(csrf())
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Authorization", matchesPattern("^Bearer [a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(jsonPath("$.type", is("Bearer")))
                    .andExpect(jsonPath("$.accessToken", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(jsonPath("$.refreshToken", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(jsonPath("$.expireTime", is((int)jwtProperties.getAccessTokenValidityInSeconds())));

        verify(accountRepository).findOneWithAuthoritiesByEmail(loginDTO.getEmail());
    }

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public JWTProperties jwtProperties() {
            JWTProperties props = new JWTProperties();
            props.setSecret("RxpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZS9kaXJlY3QdjfksjdkfjkejktZGVhbC9kaXJlY3QtZGVhbC1hY2NvdW50LXNlcnZpY2UvZGlyZWN0LWRlYWwvZGlyZWN0LWRlYWwtYWNjb3VudC1zZXJ2aWNlL2RpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZQo=");
            return props;
        }

        @Bean
        public TokenProvider tokenProvider(JWTProperties jwtProperties) {
            return new TokenProvider(jwtProperties);
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authz -> authz
                            .requestMatchers("/auth/login").permitAll()  // 로그인은 인증 없이 허용
                            .anyRequest().authenticated()
                    );
                    //.csrf(csrf -> csrf.disable()); // 테스트용으로 CSRF 비활성화

            return http.build();
        }
    }
}
