package kr.co.directdeal.saleservice.application.service.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing image information related to an item.
 * Contains a check identifier, a check URL, and a list of image URLs.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemImageDTO {

    /**
     * Identifier used for image verification or checking.
     */
    private String checkId;

    /**
     * URL used for image verification or checking.
     */
    private String checkURL;

    /**
     * List of image URLs associated with the item.
     * Defaults to an empty list.
     */
    @Builder.Default
    private List<String> images = new ArrayList<>();
}
