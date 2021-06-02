package kr.co.directdeal.sale.catalogservice.query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "SALE_ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleItem {
    @NotNull
    @Id
    private String id;

    @Column
    private String ownerId;

    @Column
    private String title;

    @Column
    private String category;

    @Column
    private long targetPrice;

    @Column
    private String text;

    @Column
    private String imagePath;

    @Column
    private String status;

    // @CreatedBy
    // @Column
    // private String createdBy;

    // @CreatedDate
    // @Column
    // private Instant createdDate; 

    // @LastModifiedBy
    // @Column
    // private String lastModifiedBy;

    // @LastModifiedDate
    // @Column
    // private Instant lastModifiedByDate; 
}
