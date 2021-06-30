package kr.co.directdeal.sale.catalogservice.webflux.domain;

import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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
    @Size(min = 36, max = 36)
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 128)
    private String title;

    @NotNull
    @Size(min = 1, max = 128)
    private String category;

    @NotNull
    @Positive
    private long targetPrice;

    private boolean discountable;

    @NotNull
    @Size(min = 6, max = 41)
    private String mainImage;

    @NotNull
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
