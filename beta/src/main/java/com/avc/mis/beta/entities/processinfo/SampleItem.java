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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.validation.groups.OnPersist;

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
	@NotNull(message = "Item is mandatory", groups = OnPersist.class)
	private Item item;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Sample item measure unit is mandatory")
	private MeasureUnit measureUnit;
	
	@Column(nullable = false, precision = 19, scale = MeasureUnit.SCALE)
	@NotNull(message = "Empty container avarage weight is mandatory")
	private BigDecimal sampleContainerWeight;
		
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "sampleItem", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Sample item requires at least one item weight")
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

}
