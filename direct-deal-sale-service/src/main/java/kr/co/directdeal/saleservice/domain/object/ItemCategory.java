package kr.co.directdeal.saleservice.domain.object;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing an item category, which can have a hierarchical
 * structure with parent and child categories.
 *
 * This entity supports parent-child relationships allowing for nested
 * categories.
 *
 * @author Cheol Jeon
 */
@Entity
@Table(name = "ITEM_CATEGORY",
        indexes = {
                @Index(columnList = "PARENT_CATEGORY_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ItemCategory {
    /**
     * Unique identifier of the item category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_CATEGORY_ID")
    private Long id;

    /**
     * Name of the item category.
     */
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "ITEM_CATEGORY_NAME", length = 128, nullable = false)
    private String name;

    /**
     * Parent category of this category, if any.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CATEGORY_ID", nullable = true)
    private ItemCategory parent;

    /**
     * List of child categories under this category.
     */
    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCategory> child = new ArrayList<>();

    /**
     * Adds a child category to this category and sets this category as its parent.
     *
     * @param itemCategory the child category to add
     */
    public void addChildItemCategory(ItemCategory itemCategory) {
        this.child.add(itemCategory);
        itemCategory.setParent(this);
    }
}
