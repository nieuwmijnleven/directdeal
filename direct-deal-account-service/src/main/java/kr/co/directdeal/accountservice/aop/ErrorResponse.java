package kr.co.directdeal.accountservice.aop;

import lombok.Data;

/**
 * Standard error response model used by global exception handlers.
 *
 * <p>This class is used to return a structured JSON error response to the client,
 * including a general error type and a detailed message.</p>
 *
 * <pre>
 * Example JSON:
 * {
 *   "error": "Validation Error",
 *   "message": "Email must not be blank"
 * }
 * </pre>
 *
 * @author Cheol Jeon
 */
@Data
public class ErrorResponse {

    /**
     * A brief description of the error type (e.g., "Validation Error", "Authentication Failed").
     */
    private String error;

    /**
     * A detailed error message that explains the reason for the failure.
     */
    private String message;

    /**
     * Constructs an {@code ErrorResponse} with the specified error type and message.
     *
     * @param error   a short label describing the error category
     * @param message a human-readable explanation of the error
     */
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
