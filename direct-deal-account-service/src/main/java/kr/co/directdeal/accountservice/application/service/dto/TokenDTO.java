package kr.co.directdeal.accountservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing authentication tokens issued after login.
 *
 * <p>Contains the token type (e.g., Bearer), access token, refresh token,
 * and the expiration time of the access token in seconds.</p>
 *
 * <pre>
 * Example JSON:
 * {
 *   "type": "Bearer",
 *   "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
 *   "expireTime": 3600
 * }
 * </pre>
 *
 * @author Cheol Jeon
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    /**
     * The type of the token, typically "Bearer".
     */
    private String type;

    /**
     * The JWT access token string.
     */
    private String accessToken;

    /**
     * The JWT refresh token string.
     */
    private String refreshToken;

    /**
     * The validity duration of the access token in seconds.
     */
    private long expireTime;
}
