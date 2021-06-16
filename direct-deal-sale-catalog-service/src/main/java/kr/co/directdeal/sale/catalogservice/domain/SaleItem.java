package kr.co.directdeal.sale.catalogservice.domain;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.serialization.DefaultInstantDeserializer;
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
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = DefaultInstantDeserializer.class)
    private Instant createdDate; 

    // @LastModifiedBy
    // private String lastModifiedBy;

    @LastModifiedDate
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = DefaultInstantDeserializer.class)
    private Instant lastModifiedDate; 
}
