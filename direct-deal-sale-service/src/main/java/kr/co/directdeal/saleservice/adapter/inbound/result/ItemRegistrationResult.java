package kr.co.directdeal.saleservice.adapter.inbound.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemRegistrationResult {
    private String itemId;
    private String checkURI;
}
