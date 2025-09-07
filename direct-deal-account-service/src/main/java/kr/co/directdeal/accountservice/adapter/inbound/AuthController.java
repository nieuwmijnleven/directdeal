package kr.co.directdeal.accountservice.adapter.inbound;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.security.auth.jwt.JwtFilter;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.accountservice.service.dto.LoginDTO;
import kr.co.directdeal.accountservice.service.dto.TokenDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JWTProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder
                                            .getObject()
                                            .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        TokenDTO tokenDTO = TokenDTO.builder()
                                .type("Bearer")
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .expireTime(jwtProperties.getAccessTokenValidityInSeconds())
                                .build();

        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }
}
