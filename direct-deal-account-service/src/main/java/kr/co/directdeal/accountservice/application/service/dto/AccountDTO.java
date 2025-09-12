package kr.co.directdeal.accountservice.application.service.dto;

import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object for account information.
 *
 * <p>This class is used for both creating and retrieving user account data.
 * It includes validation constraints for input and controls JSON serialization
 * behavior for security-related fields.</p>
 *
 * <p>Fields like password are write-only, and audit fields (createdBy, lastModifiedBy) are ignored in JSON.</p>
 *
 * <pre>
 * Example JSON (response):
 * {
 *   "id": 1,
 *   "email": "user@example.com",
 *   "name": "John Doe",
 *   "createdDate": "2023-09-12T10:30:00Z",
 *   "lastModifiedDate": "2023-09-12T11:00:00Z",
 *   "activated": true
 * }
 * </pre>
 *
 * @author Cheol Jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountDTO {

    /**
     * Unique identifier of the account.
     */
    private Long id;

    /**
     * Email address of the user. Must be valid and between 5 and 64 characters.
     */
    @Email
    @Size(min = 5, max = 64)
    private String email;

    /**
     * User's password. This field is only accepted on input (write-only).
     * Must be between 6 and 30 characters.
     */
    @JsonProperty(access = Access.WRITE_ONLY)
    @Size(min = 6, max = 30, message = "{account.constraint.password.size.message}")
    private String password;

    /**
     * Full name of the user. Must be between 1 and 30 characters.
     */
    @Size(min = 1, max = 30, message = "{account.constraint.name.size.message}")
    private String name;

    /**
     * User who created this account. Not exposed in JSON.
     */
    @JsonIgnore
    private String createdBy;

    /**
     * Timestamp when the account was created.
     */
    private Instant createdDate;

    /**
     * User who last modified this account. Not exposed in JSON.
     */
    @JsonIgnore
    private String lastModifiedBy;

    /**
     * Timestamp when the account was last updated.
     */
    private Instant lastModifiedDate;

    /**
     * Indicates whether the account is active.
     */
    private boolean activated;
}
