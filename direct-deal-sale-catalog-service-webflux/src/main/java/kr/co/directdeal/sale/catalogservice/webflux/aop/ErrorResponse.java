package kr.co.directdeal.sale.catalogservice.webflux.aop;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message; 
    }
}
