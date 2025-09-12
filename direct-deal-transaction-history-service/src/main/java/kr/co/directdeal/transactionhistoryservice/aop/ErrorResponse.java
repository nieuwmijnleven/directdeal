package kr.co.directdeal.transactionhistoryservice.aop;

import lombok.Data;

/**
 * A simple DTO for representing error responses.
 *
 * <p>This class is used in exception handlers to return consistent and meaningful
 * error information to the client.</p>
 *
 * @author Cheol Jeon
 */
@Data
public class ErrorResponse {

    /**
     * A brief error type description.
     */
    private String error;

    /**
     * A detailed message describing the cause of the error.
     */
    private String message;

    /**
     * Constructs an ErrorResponse with specified error and message.
     *
     * @param error the short error title
     * @param message the detailed error message
     */
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
