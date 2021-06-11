package kr.co.directdeal.sale.catalogservice.domain;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document("SALE_ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleItem {
    @NotNull
    // @Id
    private String id;

    private String ownerId;

    private String title;

    private String category;

    private long targetPrice;

    private boolean discountable;

    private String text;

    private List<String> images;

    private SaleItemStatus status;

    // @CreatedBy
    // private String createdBy;

    @CreatedDate
    private Instant createdDate; 

    // @LastModifiedBy
    // private String lastModifiedBy;

    @LastModifiedDate
    private Instant lastModifiedDate; 
}
