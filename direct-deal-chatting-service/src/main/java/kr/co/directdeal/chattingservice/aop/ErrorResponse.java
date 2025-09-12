package kr.co.directdeal.chattingservice.aop;

import lombok.Data;

/**
 * DTO class for API error responses.
 *
 * Holds error information including the error type and message
 * to be returned to the client.
 */
@Data
public class ErrorResponse {

    /**
     * The type or name of the error.
     */
    private String error;

    /**
     * Detailed message explaining the error.
     */
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
