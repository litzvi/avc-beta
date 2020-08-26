/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.tuple.Pair;

import com.avc.mis.beta.dto.processinfo.BasicStorageDTO;
import com.avc.mis.beta.dto.processinfo.StorageTableDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;
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
@Table(name = "PROCESSED_ITEMS")
@Inheritance(strategy=InheritanceType.JOINED)
public class ProcessItem extends ProcessInfoEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "itemId", nullable = false)
	@NotNull(message = "Item is mandatory")
	private Item item;

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "processItem", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Process line has to contain at least one storage line")
	Set<Storage> storageForms = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore
	@Column(nullable = false)
	private boolean tableView = false;
	
	/**
	 * Gets the list of Storage forms as an array (can be ordered).
	 * @return the storageForms
	 */
	public Storage[] getStorageForms() {
		return this.storageForms.toArray(new Storage[this.storageForms.size()]);
	}

	/**
	 * Setter for adding Storage forms for items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param storageForms the storageForms to set
	 */
	public void setStorageForms(Storage[] storageForms) {
		Ordinal.setOrdinals(storageForms);
		this.storageForms = Insertable.setReferences(storageForms, (t) -> {t.setReference(this);	return t;});
	}
	
	
	/**
	 * Setter for adding list of Storage forms that share the same common measure unit, 
	 * empty container weight and each only have one unit.
	 * Usefully presented in a table or list of only ordinal (number) and amount,
	 * since they all share all other parameters.
	 * @param storageTable
	 */
	public void setStorage(StorageTableDTO storageTable) {
		this.tableView = true;
		
		MeasureUnit measureUnit = storageTable.getMeasureUnit();
		BigDecimal containerWeight = storageTable.getContainerWeight();
		Warehouse warehouse = storageTable.getWarehouseLocation();
		List<BasicStorageDTO> amounts = storageTable.getAmounts();
		this.storageForms = new HashSet<>();
		amounts.forEach((amount) ->  {
					Storage storage = new Storage();
					storage.setOrdinal(amount.getOrdinal());
					storage.setUnitAmount(new AmountWithUnit(amount.getAmount(), measureUnit));
					storage.setContainerWeight(containerWeight);
					storage.setWarehouseLocation(warehouse);
					storage.setReference(this);
					this.storageForms.add(storage);
				});
	}
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
}
