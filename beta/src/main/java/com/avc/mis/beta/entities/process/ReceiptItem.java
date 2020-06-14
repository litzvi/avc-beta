/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
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
	
	/**
	 * Setter for adding Extra Added for purchase receipts, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param extraAdded the extraAdded to set
	 */
	public void setExtraAdded(ExtraAdded[] extraAdded) {
		this.extraAdded = Insertable.setReferences(extraAdded, (t) -> {t.setReference(this);	return t;});
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderItemId")
	private OrderItem orderItem;
	
	@AttributeOverrides({
        @AttributeOverride(name="amount",
                           column=@Column(name="extraRequested", precision = 19, scale = AmountWithUnit.SCALE))
    })
	@Embedded
	private AmountWithUnit extraRequested;
	
	
//	@Column(precision = 19, scale = 3)
//	private BigDecimal extraRequested;
//	
//	@Enumerated(EnumType.STRING)
////	@Column(nullable = false)
//	private MeasureUnit measureUnit;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@PrePersist
	@Override
	public void prePersist() {
		prePersistAndUpdate();		
	}
	
	@PreUpdate
	@Override
	public void preUpdate() {
		prePersistAndUpdate();
	}
	
	private void prePersistAndUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException(this.getIllegalMessage());
		this.storageForms.addAll(this.extraAdded);
	}
}
