/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

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
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.validation.groups.PositiveAmount;

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
@PrimaryKeyJoinColumn(name = "processItemId")
public class ReceiptItem extends ProcessItem {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@Transient
	private Set<ExtraAdded> extraAdded = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderItemId")
	private OrderItem orderItem;
	
	/**
	 * Setter for adding Extra Added for purchase receipts, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param extraAdded the extraAdded to set
	 */
	public void setExtraAdded(ExtraAdded[] extraAdded) {
		this.storageForms.removeAll(this.extraAdded);
		Ordinal.setOrdinals(extraAdded);
		this.extraAdded = Insertable.setReferences(extraAdded, (t) -> {t.setReference(this);	return t;});
		this.storageForms.addAll(this.extraAdded);
	}
		
	public void setStorageForms(StorageWithSample[] storageForms) { 
		super.setStorageForms(storageForms);
		this.storageForms.addAll(this.extraAdded);
	}
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="extraRequested", precision = 19, scale = MeasureUnit.SCALE))
    })
	@Embedded
	@Valid
	@ConvertGroup(from = Default.class, to = PositiveAmount.class)
	private AmountWithUnit extraRequested;

	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
}
