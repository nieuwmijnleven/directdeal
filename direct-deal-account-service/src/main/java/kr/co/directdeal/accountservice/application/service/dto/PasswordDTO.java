package kr.co.directdeal.accountservice.application.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object for changing user passwords.
 *
 * <p>Contains the current password and the new password with validation constraints.
 * Provides a utility method to check if the two passwords are the same, considering
 * validation rules.</p>
 *
 * @author Cheol Jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PasswordDTO {

    /**
     * The current password of the user.
     * Must be between 6 and 30 characters.
     */
    @NotNull
    @Size(min = 6, max = 30, message = "{account.constraint.password.size.message}")
    private String password;

    /**
     * The new password to set.
     * Must be between 6 and 30 characters.
     */
    @NotNull
    @Size(min = 6, max = 30, message = "{account.constraint.password.size.message}")
    private String newPassword;

    /**
     * Checks whether the current password and new password are both non-empty,
     * satisfy the size constraints, and are exactly equal.
     *
     * @return {@code true} if both passwords are valid and equal, {@code false} otherwise
     */
    public boolean isSamePasswords() {
        return StringUtils.hasText(this.password)
                && StringUtils.hasText(this.newPassword)
                && (6 <= this.password.length() && this.password.length() <= 30)
                && (6 <= this.newPassword.length() && this.newPassword.length() <= 30)
                && (this.password.equals(this.newPassword));
    }
}
