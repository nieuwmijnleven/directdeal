package kr.co.directdeal.sale.catalogservice.query;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SALE_LIST_ITEMS")
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

    @Column
    private String title;

    @Column
    private String category;

    @Column
    private long targetPrice;

    @Column
    private String mainImage;

    @Enumerated(EnumType.STRING)
    private SaleItemStatus status;

    @CreatedDate
    @Column
    private Instant createdDate; 

    // @LastModifiedDate
    // @Column
    // private Instant lastModifiedByDate; 
}
