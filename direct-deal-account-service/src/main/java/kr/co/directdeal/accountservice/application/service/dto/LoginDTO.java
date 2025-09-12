package kr.co.directdeal.accountservice.application.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used for user login requests.
 *
 * <p>This DTO carries the user's credentials (email and password) for authentication.
 * Validation is applied to ensure the required format and size constraints.</p>
 *
 * <pre>
 * Example JSON:
 * {
 *   "email": "user@example.com",
 *   "password": "securePassword123"
 * }
 * </pre>
 *
 * @author Cheol Jeon
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    /**
     * The user's email address.
     * Must be between 5 and 64 characters.
     */
    @NotNull
    @Size(min = 5, max = 64)
    private String email;

    /**
     * The user's password.
     * Must be between 6 and 30 characters.
     */
    @NotNull
    @Size(min = 6, max = 30, message = "{account.constraint.password.size.message}")
    private String password;
}
