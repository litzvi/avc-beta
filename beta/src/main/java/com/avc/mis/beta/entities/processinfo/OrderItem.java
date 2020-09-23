/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
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
 * Represents an order line in a purchase order.
 * Specifies the item, ordered amounts, price, 
 * delivery date and contract agreement regarding max defects.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
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
                           	precision = 19, scale = MeasureUnit.SCALE)),
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
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveOrZeroAmount.class)
	private AmountWithCurrency unitPrice;
	
	@Column(nullable = false)
	@NotNull(message = "Order item delivery date is mandatory")
	@Convert(converter = LocalDateToLong.class)
	private LocalDate deliveryDate;
	
	private String defects;//maybe change to enum that can get percentage
	
	@OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY)
	private Set<ReceiptItem> receiptItems;
	
	public void setDeliveryDate(String deliveryDate) {
		if(deliveryDate != null)
			this.deliveryDate = LocalDate.parse(deliveryDate);
	}
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
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
