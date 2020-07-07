/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.utilities.LocalDateToLong;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.avc.mis.beta.validation.groups.PositiveAmount;
import com.avc.mis.beta.validation.groups.PositiveOrZeroAmount;

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
public class OrderItem extends AuditedEntity {
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "POid", updatable = false, nullable = false)
	private PO po;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	@NotNull(message = "Item is mandatory", groups = OnPersist.class)
	private Item item;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="numberUnits", nullable = false, 
                           	precision = 19, scale = AmountWithUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(nullable = false))
    })
	@Embedded
	@NotNull(message = "number of units is mandatory")
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit numberUnits;

	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="unitPrice"))    })
	@Embedded
	@NotNull(message = "unit price is mandatory")
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveOrZeroAmount.class)
	private AmountWithCurrency unitPrice;
	
	@Convert(converter = LocalDateToLong.class)
	private LocalDate deliveryDate;
	
	private String defects;//maybe change to enum that can get percentage
	
	public void setDeliveryDate(String deliveryDate) {
		if(deliveryDate != null)
			this.deliveryDate = LocalDate.parse(deliveryDate);
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}

//	@PrePersist @PreUpdate 
//	public void prePersist() {
//		if(this.numberUnits.getMeasureUnit() == null) {
//			this.numberUnits.setMeasureUnit(item.getMeasureUnit());
//		}
//	}
	
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
