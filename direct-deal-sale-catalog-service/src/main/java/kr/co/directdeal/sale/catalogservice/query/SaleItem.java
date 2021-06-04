package kr.co.directdeal.sale.catalogservice.query;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    // @ElementCollection(fetch = FetchType.EAGER)
    @ElementCollection
    @CollectionTable(name = "SALE_ITEM_IMAGE", joinColumns = @JoinColumn(name = "SALE_ITEM_ID"))
    @Column(name = "IMAGE_PATH")
    private List<String> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SaleItemStatus status;

    // @CreatedBy
    // @Column
    // private String createdBy;

    @CreatedDate
    @Column
    private Instant createdDate; 

    // @LastModifiedBy
    // @Column
    // private String lastModifiedBy;

    // @LastModifiedDate
    // @Column
    // private Instant lastModifiedByDate; 
}
