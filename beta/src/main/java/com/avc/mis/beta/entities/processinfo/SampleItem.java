/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "SAMPLE_ITEMS")
public class SampleItem extends ProcessInfoEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	//moved to ItemWeight
//	@AttributeOverrides({
//        @AttributeOverride(name="amount",
//                           column=@Column(name="unitAmount", nullable = false, 
//                           	precision = 19, scale = AmountWithUnit.SCALE)),
//        @AttributeOverride(name="measureUnit",
//                           column=@Column(nullable = false))
//    })
//	@Embedded
//	private AmountWithUnit amountWeighed;
//	
//	@Column(nullable = false, precision = 19, scale = 3)
//	private BigDecimal amountWeighed;
//	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, precision = 19, scale = AmountWithUnit.SCALE)
	private BigDecimal emptyContainerWeight;
		
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "sampleItem", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@BatchSize(size = BaseEntity.BATCH_SIZE)
	private Set<ItemWeight> itemWeights = new HashSet<>();
	
	public ItemWeight[] getItemWeights() {
		return (ItemWeight[])this.itemWeights.toArray(new ItemWeight[this.itemWeights.size()]);
	}

	public void setItemWeights(ItemWeight[] itemWeights) {
		this.itemWeights = Insertable.setReferences(itemWeights, (t) -> {t.setReference(this);	return t;});
	}
		
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null && measureUnit != null  && emptyContainerWeight != null
				&& itemWeights.size() > 0;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Sample weight must specify an item, measure unit "
				+ "and empty container weight.";
	}

}
