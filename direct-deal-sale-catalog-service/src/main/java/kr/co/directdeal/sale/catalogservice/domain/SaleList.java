package kr.co.directdeal.sale.catalogservice.domain;

import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document("SALE_LIST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleList {
    @NotNull
    @Id
    private String id;

    private String title;

    private String category;

    private long targetPrice;

    private boolean discountable;

    private String mainImage;

    private SaleItemStatus status;

    @CreatedDate
    private Instant createdDate; 

    @LastModifiedDate
    private Instant lastModifiedDate; 

    public static SaleList copyOf(SaleList another) {
        return SaleList.builder()
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
