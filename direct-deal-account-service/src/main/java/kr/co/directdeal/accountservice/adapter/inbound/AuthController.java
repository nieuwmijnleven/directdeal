package kr.co.directdeal.accountservice.adapter.inbound;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import kr.co.directdeal.common.security.auth.jwt.JwtFilter;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.accountservice.application.service.dto.LoginDTO;
import kr.co.directdeal.accountservice.application.service.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

/**
 * REST controller responsible for handling user authentication.
 *
 * <p>Handles login requests and returns JWT access/refresh tokens upon successful authentication.</p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JWTProperties jwtProperties;

    private final CsrfTokenRepository csrfTokenRepository;

    /**
     * Authenticates a user using email and password credentials and returns JWT tokens.
     *
     * @param loginDto the login request containing email and password
     * @return JWT access and refresh tokens in response headers and body
     */
    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT access token and refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, token returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input format")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @Parameter(description = "Login credentials (email and password)", required = true)
            @Valid @RequestBody LoginDTO loginDto) {

        // Create authentication token using provided email and password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // Perform authentication
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        // Set authenticated user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT tokens
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        // Prepare response headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                                            .httpOnly(true)
                                            .secure(true)
                                            .sameSite("Strict")
                                            .path("/")
                                            .build();
        httpHeaders.add("Set-Cookie", cookie.toString());

        // Create response body with token information
        TokenDTO tokenDTO = TokenDTO.builder()
                .type("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expireTime(jwtProperties.getAccessTokenValidityInSeconds())
                .build();

        // Return response with token in headers and body
        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @Parameter(description = "refreshToken", required = false)
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            @Parameter(description = "XSRF-TOKEN", required = false)
            @CookieValue(value = "XSRF-TOKEN", required = false) String csrfCookieToken,
            @Parameter(description = "X-XSRF-TOKEN", required = false)
            @RequestHeader(value = "X-XSRF-TOKEN", required = false) String csrfHeaderToken,
            @Parameter(description = "_csrf", required = false)
            @RequestParam(value = "_csrf", required = false) String csrfParamToken)  {

        String csrfRequestToken = csrfHeaderToken != null ? csrfHeaderToken : csrfParamToken;
        if (csrfCookieToken == null || csrfRequestToken == null || !csrfCookieToken.equals(csrfRequestToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CSRF token validation failed");
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }

        if (!tokenProvider.validateToken(refreshToken))
            return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);

        //retrieve database to find refresh_token

        //make authentication
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        //retrieve database to find user

        // Set authenticated user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT tokens
        String newAccessToken = tokenProvider.createAccessToken(authentication);
        String newRefreshToken = tokenProvider.createRefreshToken(authentication);

        // Prepare response headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + newAccessToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
        httpHeaders.add("Set-Cookie", cookie.toString());

        // Create response body with token information
        TokenDTO tokenDTO = TokenDTO.builder()
                .type("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expireTime(jwtProperties.getAccessTokenValidityInSeconds())
                .build();

        // Return response with token in headers and body
        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken token = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(token, request, response);
        return token;
    }
}
