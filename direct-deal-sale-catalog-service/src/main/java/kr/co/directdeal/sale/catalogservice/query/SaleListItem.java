package kr.co.directdeal.sale.catalogservice.query;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document("SALE_LIST_ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleListItem {
    @NotNull
    @Id
    private String id;

    private String title;

    private String category;

    private long targetPrice;

    private String mainImage;

    private SaleItemStatus status;

    @CreatedDate
    private Instant createdDate; 

    // @LastModifiedDate
    // private Instant lastModifiedByDate; 

    public static SaleListItem copyOf(SaleListItem another) {
        return SaleListItem.builder()
                    .id(another.getId())
                    .title(another.getTitle())
                    .category(another.getCategory())
                    .targetPrice(another.getTargetPrice())
                    .mainImage(another.getMainImage())
                    .status(another.getStatus())
                    .createdDate(another.getCreatedDate())
                    .build();
    }
}
