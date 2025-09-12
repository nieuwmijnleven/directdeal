package kr.co.directdeal.saleservice.aop;

import lombok.Data;

/**
 * Represents a standardized error response returned by API endpoints
 * when an exception or error occurs.
 *
 * Contains an error type or category and a detailed error message.
 * This class is typically used in exception handling to provide
 * clients with meaningful error information.
 *
 * @author Cheol Jeon
 */
@Data
public class ErrorResponse {

    /**
     * The error type or category.
     */
    private String error;

    /**
     * The detailed error message describing the cause.
     */
    private String message;

    /**
     * Constructs an ErrorResponse with the specified error and message.
     *
     * @param error the error type or category
     * @param message the detailed error message
     */
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
