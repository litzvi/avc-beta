/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

import com.avc.mis.beta.dto.process.inventory.BasicStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageTableDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.process.inventory.UsedItemBase;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "group", targetEntity=UsedItemBase.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@Where(clause = "dtype = 'Storage'") //only storage belongs to this group - storageMove belongs to StorageMovesGroup
//	@NotEmpty(message = "Process line has to contain at least one storage line") //made a bug when using merge for persisting ProcessItem
	private Set<Storage> storageForms = new HashSet<>();
	
	@JsonIgnore
	@ToString.Exclude 
	@OneToMany(mappedBy = "processItem", fetch = FetchType.LAZY)
	private Set<StorageBase> allStorages;
	
	/**
	 * Gets the list of Storage forms as an array (can be ordered).
	 * @return the storageForms
	 */
	public Storage[] getStorageForms() {
		Storage[] storageForms = this.storageForms.toArray(new Storage[this.storageForms.size()]);
		Arrays.sort(storageForms, Ordinal.ordinalComparator());
		return storageForms;
	}
	
	@JsonIgnore
	Set<Storage> getStorageFormsField() {
		return this.storageForms;
	}

	/**
	 * Setter for adding Storage forms for items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param storageForms array of Storages to set
	 */
	public void setStorageForms(Storage[] storageForms) {
		Ordinal.setOrdinals(storageForms);
		this.storageForms = Insertable.setReferences(storageForms, (t) -> {t.setReference(this);	return t;});
	}
	
	/**
	 * Setter for adding Storage forms for items that are processed (used items), 
	 * the new storages will have the same form as the used ones with the given new location.
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param usedItems array of UsedItemDTOs that are used
	 */
	public void setUsedItems(UsedItemDTO[] usedItems) {
		try {
			setStorageForms((Storage[]) Arrays.stream(usedItems)
					.map(i -> i.getNewStorage())
					.toArray());
		} catch (NullPointerException e) {
			throw new NullPointerException("Used item storage is null");
		}		
	}
	
	/**
	 * Setter for adding list of Storage forms that share the same common measure unit, 
	 * empty container weight and each only have one unit.
	 * Usefully presented in a table or list of only ordinal (number) and amount,
	 * since they all share all other parameters.
	 * @param storageTable
	 */
	public void setStorage(StorageTableDTO storageTable) {
		
		
		setTableView(true);
		
		BigDecimal accessWeight = storageTable.getAccessWeight();
		Warehouse warehouse = storageTable.getWarehouseLocation();
		List<BasicStorageDTO> amounts = storageTable.getAmounts();
		Storage[] storageForms = new Storage[amounts.size()];
		for(int i=0; i<storageForms.length; i++) {
			BasicStorageDTO amount = amounts.get(i);
			storageForms[i] = new Storage();
			storageForms[i].setId(amount.getId());
			storageForms[i].setVersion(amount.getVersion());
			storageForms[i].setOrdinal(amount.getOrdinal());
			storageForms[i].setNumberUnits(amount.getAmount());
			storageForms[i].setAccessWeight(accessWeight);
			storageForms[i].setWarehouseLocation(warehouse);
		}
		setStorageForms(storageForms);
		
	}
	
	@PrePersist @PreUpdate
	public void measureUnitItemCompatiable() {
		Item.measureUnitItemCompatiable(item.getMeasureUnit(), getMeasureUnit());
//		MeasureUnit itemDefaultMU = item.getMeasureUnit();
//		if(MeasureUnit.DISCRETE_UNITS.contains(itemDefaultMU) ^ MeasureUnit.DISCRETE_UNITS.contains(getMeasureUnit())) {
//			if(MeasureUnit.DISCRETE_UNITS.contains(itemDefaultMU)) {
//				throw new IllegalArgumentException("Discrete item can't have a weight measure unit");
//			}
//			else {
//				throw new IllegalArgumentException("Bulk weight item can't have a Discrete measure unit");
//			}
//		}
	}
	

}
