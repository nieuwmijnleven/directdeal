package kr.co.directdeal.saleservice.domain.object;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

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
    private Long id;

    @NotNull
    @Column(name = "USER_ID", length = 64, unique = true)
    private String userId;

    @NotNull
    @Column(name = "ITEM_ID", length = 36, nullable = false)
    private String itemId;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private Instant createdDate; 
}
