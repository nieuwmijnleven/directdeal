package kr.co.directdeal.sale.catalogservice.adapter.inbound.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Response object for the LiftUp operation.
 *
 * @author Cheol Jeon
 */
@Data
@AllArgsConstructor
@Builder
public class LiftUpResponse {
    private ResultConstants result;
    private int intervalDays;

    public enum ResultConstants {
        SUCCESS,
        FAILURE
    }
}
