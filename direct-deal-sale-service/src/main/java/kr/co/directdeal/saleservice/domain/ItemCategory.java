package kr.co.directdeal.saleservice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ITEM_CATEGORY", 
        indexes = {
            @Index(columnList = "PARENT_CATEGORY_ID")
        })
// @Table(name = "ITEM_CATEGORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ItemCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_CATEGORY_ID")
    private Long id;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "ITEM_CATEGORY_NAME", length = 128, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CATEGORY_ID", nullable = true)
    private ItemCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCategory> child = new ArrayList<>();

    public void addChildItemCategory(ItemCategory itemCategory) {
        this.child.add(itemCategory);
        itemCategory.setParent(this);
    }
}
