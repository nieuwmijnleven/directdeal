package kr.co.directdeal.saleservice.adapter.inbound.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the result of an item registration operation,
 * including the generated item ID and a URI for checking the item.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemRegistrationResult {

    /**
     * The unique identifier of the registered item.
     */
    private String itemId;

    /**
     * The URI to check or access the registered item.
     */
    private String checkURI;
}
