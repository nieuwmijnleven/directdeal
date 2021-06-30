package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
