package kr.co.directdeal.saleservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing an item category.
 * It includes the category's ID, name, parent category, and child categories.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCategoryDTO {

    /**
     * Unique identifier of the item category.
     */
    private Long id;

    /**
     * Name of the item category.
     */
    private String name;

    /**
     * Parent category of this item category.
     */
    private ItemCategoryDTO parent;

    /**
     * List of child categories under this item category.
     */
    @Builder.Default
    private List<ItemCategoryDTO> child = new ArrayList<>();
}
