/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.Item;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class OrderItem extends BaseEntity {
	
//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POid", updatable = false, nullable = false)
	private PO po;
	
	@ManyToOne
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	private BigDecimal numberUnits;
	
	
	@Transient
	private MeasureUnit measureUnit;
	
	@Setter(value = AccessLevel.NONE)
	private Currency currency;
	
//	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	private BigDecimal unitPrice;
	
	@Temporal(TemporalType.DATE)
	private Calendar deliveryDate;
	private String defects;//maybe change to enum that can get percentage
	private String remarks;
	
	public void setCurrency(String currencyCode) {
		if(currencyCode != null)
			this.currency = Currency.getInstance(currencyCode);
	}
	
	
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = MeasureUnit.valueOf(measureUnit);
	}
	
//	public String getMeasureUnit() {
//		return Optional.ofNullable(this.item).map(i -> i.getMeasureUnit().toString()).orElse(null);
//	}
	
		
//	public void setUnitPrice(String val) {
//		this.unitPrice = new BigDecimal(val);
//	}
//	
//	public String getUnitprice() {
//		return this.unitPrice.toPlainString();
//	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@Override
	public boolean isLegal() {
		return item != null;
	}

	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal()) {
			throw new IllegalArgumentException(
					"Order line is not legal has to reference an item");
		}
		if(this.measureUnit != null && this.measureUnit != this.item.getMeasureUnit()) {			
			this.numberUnits = MeasureUnit.convert(
					this.numberUnits, this.measureUnit, this.item.getMeasureUnit());
		}
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

}
