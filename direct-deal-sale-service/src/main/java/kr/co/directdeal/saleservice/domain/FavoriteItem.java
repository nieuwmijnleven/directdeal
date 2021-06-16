package kr.co.directdeal.saleservice.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "FAVORITE_ITEM", 
        indexes = {
            @Index(unique = true, columnList = "USER_ID, ITEM_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class FavoriteItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAVORITE_ITEM_ID")
    private String id;

    @NotNull
    @Column(name = "USER_ID", length = 50, unique = true)
    private String userId;

    @NotNull
    @Column(name = "ITEM_ID", length = 36, nullable = false)
    private String itemId;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private Instant createdDate; 
}
