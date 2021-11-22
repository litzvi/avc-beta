/**
 * 
 */
package com.avc.mis.beta.entities.process.group;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.process.storages.StorageBase;
import com.avc.mis.beta.entities.process.storages.UsedItemBase;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents an Item that the process adds to stock. perhaps name should be changed to InItem/ImportItem/AddedItem
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "PROCESSED_ITEMS")
@PrimaryKeyJoinColumn(name = "groupId")
public class ProcessItem extends ProcessGroupWithStorages {
	
	{
		setDtype("ProcessItem");
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "Item is mandatory")
	@JoinColumn(name = "itemId", nullable = false)
	private Item item;

	@OneToMany(mappedBy = "group", targetEntity=UsedItemBase.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@Where(clause = "dtype = 'Storage'") //only storage belongs to this group - storageMove belongs to StorageMovesGroup
//	@NotEmpty(message = "Process line has to contain at least one storage line") //made a bug when using merge for persisting ProcessItem
	private Set<Storage> storageForms = new HashSet<>();
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "processItem", fetch = FetchType.LAZY)
	private Set<StorageBase> allStorages;
	
	@JsonIgnore
	protected Set<Storage> getStorageFormsField() {
		return this.storageForms;
	}

	/**
	 * Setter for adding Storage forms for items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param storageForms array of Storages to set
	 */
	public void setStorageForms(Set<Storage> storageForms) {
		this.storageForms = Insertable.setReferences(storageForms, (t) -> {t.setReference(this);	return t;});
	}
		
	@PrePersist @PreUpdate
	public void measureUnitItemCompatiable() {
		if(getItem() != null && getMeasureUnit() != null)
			Item.measureUnitItemCompatiable(getItem().getMeasureUnit(), getMeasureUnit());
	}
	

}
