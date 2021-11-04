/**
 * 
 */
package com.avc.mis.beta.entities.process.collection;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ITEM_COUNTS")
@Inheritance(strategy=InheritanceType.JOINED)
public class ItemCount extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", nullable = false)
	@NotNull(message = "Item is mandatory")
	private Item item;
		
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Measure unit required")
	private MeasureUnit measureUnit;
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal containerWeight;	
		
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal accessWeight;

	@Setter(value = AccessLevel.NONE) 
//	@Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "itemCount", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@NotEmpty(message = "Sample has to contain at least one sampled amount")
	private Set<CountAmount> amounts;
	
//	public CountAmount[] getAmounts() {
//		CountAmount[] amounts = this.amounts.toArray(new CountAmount[this.amounts.size()]);
//		Arrays.sort(amounts, Ordinal.ordinalComparator());	
//		return amounts;
//	}
	
	public void setAmounts(Set<CountAmount> amounts) {
//		Ordinal.setOrdinals(amounts);
		this.amounts = Insertable.setReferences(amounts, (t) -> {t.setReference(this);	return t;});
	}
	
		
}
