package kr.co.directdeal.transactionhistoryservice.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "TRANSACTION_HISTORY", 
        indexes = {
            @Index(unique = false, columnList = "TRANSACTION_ITEM_ID, TRANSACTION_ITEM_SELLER_ID"),
            @Index(unique = false, columnList = "TRANSACTION_ITEM_BUYER_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class TransactionHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private String id;

    @NotNull
    @Column(name = "TRANSACTION_ITEM_ID", nullable = false)
    private String itemId;

    @NotNull
    @Column(name = "TRANSACTION_ITEM_SELLER_ID", nullable = false)
    private String sellerId;

    @NotNull
    @Column(name = "TRANSACTION_ITEM_TITLE", nullable = false)
    private String title;

    @NotNull
    @Column(name = "TRANSACTION_ITEM_CATEGORY", nullable = false)
    private String category;

    @NotNull
    @Column(name = "TRANSACTION_ITEM_TARGET_PRICE", nullable = false)
    private long targetPrice;

    @Column(name = "TRANSACTION_ITEM_BUYER_ID", nullable = true)
    private String buyerId;

    @NotNull
    @Column(name = "TRANSACTION_ITEM_COMPLETION_TIME", nullable = false)
    private Instant completionTime; 
}
