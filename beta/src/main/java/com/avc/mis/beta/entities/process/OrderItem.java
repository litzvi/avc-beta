/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PO_ITEMS")
public class OrderItem extends ProcessEntity{
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POid", updatable = false, nullable = false)
	private PO po;
	
	@ManyToOne
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	private BigDecimal numberUnits;	
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MeasureUnit measureUnit;
	
	@Setter(value = AccessLevel.NONE)
	private Currency currency;
	
	private BigDecimal unitPrice;
	
	private LocalDate deliveryDate;
	private String defects;//maybe change to enum that can get percentage
	private String remarks;
	
	public void setCurrency(String currencyCode) {
		if(currencyCode != null)
			this.currency = Currency.getInstance(currencyCode);
	}
	
	
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null;
	}

	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal()) {
			throw new IllegalArgumentException(this.getIllegalMessage());
		}
		if(this.measureUnit == null) {
			this.measureUnit = item.getMeasureUnit();
		}
//		if(this.measureUnit != null && this.measureUnit != this.item.getMeasureUnit()) {			
//			this.numberUnits = MeasureUnit.convert(
//					this.numberUnits, this.measureUnit, this.item.getMeasureUnit());
//		}
	}
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof PO) {
			this.setPo((PO)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a purchase order");
		}		
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Order line is not legal has to reference an item";
	}

}
