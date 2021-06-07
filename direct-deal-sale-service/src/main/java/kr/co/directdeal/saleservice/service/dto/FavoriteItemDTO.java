package kr.co.directdeal.saleservice.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteItemDTO {
    private String id;
    private String userId;
    private String itemId;
    private Instant createdDate; 
}
