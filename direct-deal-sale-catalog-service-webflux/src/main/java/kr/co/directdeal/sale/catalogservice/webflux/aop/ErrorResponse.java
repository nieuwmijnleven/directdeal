package kr.co.directdeal.sale.catalogservice.webflux.aop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Standard structure for error responses returned from the API.
 * <p>
 * Contains a short error code and a detailed message for the client.
 * </p>
 *
 * Example:
 * <pre>
 * {
 *   "error": "Validation Error",
 *   "message": "The 'title' field must not be blank."
 * }
 * </pre>
 *
 * @author Cheol Jeon
 */
@Data
public class ErrorResponse {

    /**
     * Short label or type of the error (e.g., "Validation Error", "Server Error").
     */
    @Schema(description = "Short error type (e.g., 'Validation Error', 'Server Error')", example = "Validation Error")
    private String error;

    /**
     * Detailed error message for the client to understand the cause.
     */
    @Schema(description = "Detailed description of the error", example = "The 'title' field must not be blank.")
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
