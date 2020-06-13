/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.utilities.LocalDateToLong;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Table(name = "PO_ITEMS")
public class OrderItem extends ProcessEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POid", updatable = false, nullable = false)
	private PO po;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="numberUnits", nullable = false, precision = 19, scale = 3)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	private AmountWithUnit numberUnits;
	
//	@Column(nullable = false, precision = 19, scale = 3)
//	private BigDecimal numberUnits;	
//	
//	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
//	private MeasureUnit measureUnit;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="unitPrice"))    })
	@Embedded
	private AmountWithCurrency unitPrice;
	
//	@Setter(value = AccessLevel.NONE)
//	private Currency currency;
//	
//	@Column(precision = 19, scale = 2)
//	private BigDecimal unitPrice;
	
	@Convert(converter = LocalDateToLong.class)
	private LocalDate deliveryDate;
	
	private String defects;//maybe change to enum that can get percentage
	
//	@Enumerated(EnumType.STRING)
//	@Column(nullable = false)
//	private OrderItemStatus status = OrderItemStatus.OPEN;
//	
//	public void setCurrency(String currencyCode) {
//		if(currencyCode != null)
//			this.currency = Currency.getInstance(currencyCode);
//	}
	
	public void setDeliveryDate(String deliveryDate) {
		if(deliveryDate != null)
			this.deliveryDate = LocalDate.parse(deliveryDate);
	}
	
	
//	public void setMeasureUnit(String measureUnit) {
//		if(measureUnit != null)
//			this.measureUnit = MeasureUnit.valueOf(measureUnit);
//	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null && numberUnits.isFilled() && unitPrice.isFilled()
				&& numberUnits.signum() > 0
				&& unitPrice.signum() >= 0;
	}

	@PrePersist @PreUpdate 
	@Override
	public void prePersist() {
		if(this.numberUnits.getMeasureUnit() == null) {
			this.numberUnits.setMeasureUnit(item.getMeasureUnit());
		}
		if(!isLegal()) {
			throw new IllegalArgumentException(this.getIllegalMessage());
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

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Order line is not legal has to reference an item";
	}

}
