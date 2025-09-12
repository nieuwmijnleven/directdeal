package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Response object representing the result of a "lift up" operation.
 * <p>
 * Contains the outcome of the operation and the remaining days
 * before the next lift-up is allowed.
 * </p>
 *
 * Example use-case: Refreshing or reordering a sale item to appear at the top of a list.
 *
 * @author Cheol Jeon
 */
@Data
@AllArgsConstructor
@Builder
public class LiftUpResponse {

    /**
     * The result of the lift-up operation.
     * Indicates whether the operation was successful or failed.
     */
    private ResultConstants result;

    /**
     * The number of days remaining until the next lift-up operation is allowed.
     */
    private int intervalDays;

    /**
     * Enumeration representing possible outcomes of a lift-up operation.
     */
    public enum ResultConstants {
        /**
         * The operation was successful.
         */
        SUCCESS,

        /**
         * The operation failed.
         */
        FAILURE
    }
}
