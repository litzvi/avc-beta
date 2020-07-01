/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an Item that the process adds to stock. perhaps name should be changed to InItem/ImportItem/AddedItem
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
//@BatchSize(size = BaseEntity.BATCH_SIZE)
@Table(name = "PROCESSED_ITEMS")
@Inheritance(strategy=InheritanceType.JOINED)
public class ProcessItem extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", updatable = false, nullable = false)
	private Item item;
	
//	@ToString.Exclude
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "itemPoCode", updatable = false)
//	private PoCode itemPo; //should be null for receiving, for items used in the process - maybe removed
	
	
//	@Convert(converter = LocalDateToLong.class)
//	private LocalDate processDate;
//	
//	public void setDeliveryDate(String processDate) {
//		if(processDate != null)
//			this.processDate = LocalDate.parse(processDate);
//	}
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "processItem", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@BatchSize(size = BaseEntity.BATCH_SIZE)
	Set<Storage> storageForms = new HashSet<>();
	
	/**
	 * Gets the list of Storage forms as an array (can be ordered).
	 * @return the storageForms
	 */
	public Storage[] getStorageForms() {
		return (Storage[])this.storageForms.toArray(new Storage[this.storageForms.size()]);
	}

	/**
	 * Setter for adding Storage forms for items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param storageForms the storageForms to set
	 */
	public void setStorageForms(Storage[] storageForms) {
		this.storageForms = Insertable.setReferences(storageForms, (t) -> {t.setReference(this);	return t;});
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return item != null && storageForms.size() > 0;
//				&& unitAmount.compareTo(BigDecimal.ZERO) > 0
//				&& numberUnits.compareTo(BigDecimal.ZERO) > 0;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process item has to contain an item and at least one storage info line";
	}

}
