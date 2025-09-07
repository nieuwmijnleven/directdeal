package kr.co.directdeal.sale.catalogservice.webflux.domain;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.serialization.DefaultInstantDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document("SALE_ITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleItem {
    @NotNull
    @Size(min = 36, max = 36)
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 64)
    private String ownerId;

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
    @Size(min = 1, max = 1024)
    private String text;

    private List<String> images;

    @NotNull
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
