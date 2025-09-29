package kr.co.directdeal.accountservice.adapter.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import kr.co.directdeal.accountservice.application.service.AccountDetailService;
import kr.co.directdeal.accountservice.application.service.dto.LoginDTO;
import kr.co.directdeal.accountservice.config.TestSecurityConfig;
import kr.co.directdeal.accountservice.domain.object.Account;
import kr.co.directdeal.accountservice.domain.object.Authority;
import kr.co.directdeal.accountservice.port.outbound.AccountRepository;
import kr.co.directdeal.accountservice.util.XorCsrfTokenGenerator;
import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@Import(TestSecurityConfig.class)
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTProperties jwtProperties;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

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
                    //.with(csrf())
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
                    //.with(csrf())
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
                    //.with(csrf())
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(header().string("Authorization", matchesPattern("^Bearer [a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(header().string("Set-Cookie", matchesPattern("^refreshToken=[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?;\\sPath=/;\\sSecure;\\sHttpOnly;\\sSameSite=Strict$")))
                    .andExpect(jsonPath("$.type", is("Bearer")))
                    .andExpect(jsonPath("$.accessToken", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(jsonPath("$.refreshToken", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                    .andExpect(jsonPath("$.expireTime", is((int)jwtProperties.getAccessTokenValidityInSeconds())));

        verify(accountRepository).findOneWithAuthoritiesByEmail(loginDTO.getEmail());
    }

    @Test
    public void Refresh_WithoutCsrfToken_ReturnNotAcceptable() throws Exception {
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

        // Create authentication token using provided email and password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        // Perform authentication
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        // Set authenticated user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT tokens
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        //when and then
        this.mvc.perform(post("/auth/refresh")
                        //.with(csrf())
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void Csrf_NoCondition_ReturnCsrfToken() throws Exception {
        //given

        //when and then
        this.mvc.perform(get("/auth/csrf"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", matchesPattern("^XSRF-TOKEN=(?:[a-zA-Z0-9\\-_]*?);\sPath=/")));
    }

    @Test
    public void Refresh_WithValidCsrfTokenAndValidRefreshToken_ReturnJWT() throws Exception {
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

        // Create authentication token using provided email and password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        // Perform authentication
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        // Set authenticated user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT tokens
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        // Generate Xored CSRF Token
        XorCsrfTokenGenerator xorCsrfTokenGenerator = XorCsrfTokenGenerator.generate();
        CsrfToken xoredCsrfToken = xorCsrfTokenGenerator.getXoredCsrfToken();
        CsrfToken csrfToken = xorCsrfTokenGenerator.getCsrfToken();

        //when and then
        this.mvc.perform(post("/auth/refresh")
                        .header("X-XSRF-TOKEN", xoredCsrfToken.getToken())
                        //.with(csrf())
                        .cookie(new Cookie("refreshToken", refreshToken), new Cookie("XSRF-TOKEN", csrfToken.getToken())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", matchesPattern("^Bearer [a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                .andExpect(header().string("Set-Cookie", matchesPattern("^refreshToken=[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?; Secure;\\sHttpOnly;\\sSameSite=Strict$")))
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.accessToken", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                //.andExpect(jsonPath("$.refreshToken", matchesPattern("^[a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?$")))
                .andExpect(jsonPath("$.expireTime", is((int)jwtProperties.getAccessTokenValidityInSeconds())));

        verify(accountRepository).findOneWithAuthoritiesByEmail(loginDTO.getEmail());
    }

    @Test
    public void Refresh_WithValidCsrfTokenAndInvalidRefreshToken_ReturnNotAcceptable() throws Exception {
        //given
        String invalidRefreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhY2NvdW50QGRpcmVjdGRlYWwuY28ua3IiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImV4cCI6MTc1ODM5ODIzOH0.NH7UXHXdZGhNE9KQ0AKpcw00BZGUqGaDKUlzD6AvD_j4tS3bv_JS6uSUSBBfq53t3Iu73kxRhZGJNixRsocFVw";

        // Generate Xored CSRF Token
        XorCsrfTokenGenerator xorCsrfTokenGenerator = XorCsrfTokenGenerator.generate();
        CsrfToken xoredCsrfToken = xorCsrfTokenGenerator.getXoredCsrfToken();
        CsrfToken csrfToken = xorCsrfTokenGenerator.getCsrfToken();

        //when and then
        this.mvc.perform(post("/auth/refresh")
                        .header("X-XSRF-TOKEN", xoredCsrfToken.getToken())
                        .cookie(new Cookie("refreshToken", invalidRefreshToken), new Cookie("XSRF-TOKEN", csrfToken.getToken())))
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }
}
