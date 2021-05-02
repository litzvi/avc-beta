/**
 * 
 */
package com.avc.mis.beta.entities.process.collection;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithCurrency;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
import com.avc.mis.beta.validation.groups.PositiveAmount;
import com.avc.mis.beta.validation.groups.PositiveOrZeroAmount;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a line in a receipt of a purchased item.
 * Contains amounts received, the way they are stored and reference to the order item if existing.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "RECEIPT_ITEMS")
@PrimaryKeyJoinColumn(name = "groupId")
public class ReceiptItem extends ProcessItem {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderItemId")
	private OrderItem orderItem;

	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="receivedOrderAmount", 
                           	precision = 19, scale = MeasureUnit.SCALE)),
        @AttributeOverride(name="measureUnit",
                           column=@Column(name = "orderMeasureUnit"))
    })
	@NotNull(message = "Invoice amount is mandatory")
	@Embedded
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit receivedOrderUnits;

	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="unitPrice"))    })
	@Embedded
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveOrZeroAmount.class)
	private AmountWithCurrency unitPrice;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="extraRequested", precision = 19, scale = MeasureUnit.SCALE)),
        @AttributeOverride(name="measureUnit", 
    						column=@Column(name = "extraMeasureUnit"))
    })
	@Embedded
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit extraRequested;
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@Transient
	private Set<ExtraAdded> extraAdded = new HashSet<>();
		
	/**
	 * Setter for adding Extra Added for purchase receipts, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param extraAdded the extraAdded to set
	 */
	public void setExtraAdded(ExtraAdded[] extraAdded) {
		getStorageFormsField().removeAll(this.extraAdded);
		Ordinal.setOrdinals(extraAdded);
		this.extraAdded = Insertable.setReferences(extraAdded, (t) -> {t.setReference(this);	return t;});
		getStorageFormsField().addAll(this.extraAdded);
	}
		
	public void setStorageForms(StorageWithSample[] storageForms) { 		
		super.setStorageForms(storageForms);
		getStorageFormsField().addAll(this.extraAdded);
	}
	
}
