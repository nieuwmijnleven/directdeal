package kr.co.directdeal.accountservice.adapter.inbound;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
}
