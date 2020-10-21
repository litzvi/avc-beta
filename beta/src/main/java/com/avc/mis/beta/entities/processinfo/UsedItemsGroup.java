/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.avc.mis.beta.dto.processinfo.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemTableDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Groups used items of a transaction process, 
 * in order to connect between them for display purposes.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USED_ITEMS_GROUP")
public class UsedItemsGroup extends ProcessInfoEntity {

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "group", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@NotEmpty(message = "Has to containe at least one used storage item")
	private Set<UsedItem> usedItems = new HashSet<>();
		
	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore
	@Column(nullable = false)
	private boolean tableView = false;
	
	private String groupName;

	public void setGroupName(String groupName) {
		this.groupName = Optional.ofNullable(groupName).map(s -> s.trim()).orElse(null);
	}
	
	/**
	 * Gets the list of used items as an array (can be ordered).
	 * @return the usedItems
	 */
	public UsedItem[] getUsedItems() {
		UsedItem[] usedItems = this.usedItems.toArray(new UsedItem[this.usedItems.size()]);
		Arrays.sort(usedItems, Ordinal.ordinalComparator());
		return usedItems;
	}

	/**
	 * Setter for adding items used in a business/manufacturing process (process input), 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the null objects and sets the needed references to satisfy needed foreign keys of database.
	 * @param usedItems
	 */
	public void setUsedItems(UsedItem[] usedItems) {
		Ordinal.setOrdinals(usedItems);
		this.usedItems = Insertable.setReferences(usedItems, (t) -> {t.setReference(this);	return t;});
	}
	
	/**
	 * Setter for adding list of Used items that share the same common measure unit, 
	 * empty container weight and each only have one unit, that are usually represented as a table in ProcessItem.
	 * Used in order to match used storages from a ProcessItem that was set by corresponding setStorage(storageTable) in ProcessItem.
	 * Usefully presented in a table or list of only ordinal (number) and amount, since they all share all other parameters.
	 * @param usedItemTable
	 */
	public void setUsedItem(UsedItemTableDTO usedItemTable) {
		this.tableView = true;
		
		List<BasicUsedStorageDTO> basicUsedStorages = usedItemTable.getAmounts();
		UsedItem[] usedItems = new UsedItem[basicUsedStorages.size()];
		for(int i=0; i<usedItems.length; i++) {
			BasicUsedStorageDTO basicUsedStorage = basicUsedStorages.get(i);
			usedItems[i] = new UsedItem();
			usedItems[i].setId(basicUsedStorage.getId());
			usedItems[i].setVersion(basicUsedStorage.getVersion());
			Storage storage = new Storage();
			storage.setId(basicUsedStorage.getStorageId());
			storage.setVersion(basicUsedStorage.getStorageVersion());
			usedItems[i].setStorage(storage);
		}
		setUsedItems(usedItems);
	}


}
