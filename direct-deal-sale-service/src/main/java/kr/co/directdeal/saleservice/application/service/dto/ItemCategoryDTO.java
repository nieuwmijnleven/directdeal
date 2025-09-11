package kr.co.directdeal.saleservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCategoryDTO {
    private Long id;
    private String name;
    private ItemCategoryDTO parent;
    @Builder.Default
    private List<ItemCategoryDTO> child = new ArrayList();
}
