/**
 * 
 */
package com.avc.mis.beta.entities.process.collection;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.avc.mis.beta.entities.process.inventory.UsedItemBase;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString(callSuper = true)
@Entity
@Table(name = "USED_ITEMS_GROUP")
@PrimaryKeyJoinColumn(name = "groupId")
//@Inheritance(strategy=InheritanceType.JOINED)
public class UsedItemsGroup extends ProcessGroup {
	
	{
		setDtype("UsedItemsGroup");
	}
	
	@Setter(value = AccessLevel.NONE) 
//	@Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "group", targetEntity = UsedItemBase.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@NotEmpty(message = "Has to containe at least one used storage item") //probably because persists empty when doing merge on a new one
	private Set<UsedItem> usedItems = new HashSet<>();
	
	/**
	 * Gets the list of used items as an array (can be ordered).
	 * @return the usedItems
	 */
//	public UsedItem[] getUsedItems() {
//		UsedItem[] usedItems = this.usedItems.toArray(new UsedItem[this.usedItems.size()]);
//		Arrays.sort(usedItems, Ordinal.ordinalComparator());
//		return usedItems;
//	}

	/**
	 * Setter for adding items used in a business/manufacturing process (process input), 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the null objects and sets the needed references to satisfy needed foreign keys of database.
	 * @param usedItems
	 */
	public void setUsedItems(Set<UsedItem> usedItems) {
//		Ordinal.setOrdinals(usedItems);
		this.usedItems = Insertable.setReferences(usedItems, (t) -> {t.setReference(this);	return t;});
	}
	
	/**
	 * Setter for adding list of Used items that share the same common measure unit, 
	 * empty container weight and each only have one unit, that are usually represented as a table in ProcessItem.
	 * Used in order to match used storages from a ProcessItem that was set by corresponding setStorage(storageTable) in ProcessItem.
	 * Usefully presented in a table or list of only ordinal (number) and amount, since they all share all other parameters.
	 * @param usedItemTable
	 */
//	public void setUsedItem(UsedItemTableDTO usedItemTable) {
//			
//		setTableView(true);
//		
//		List<BasicUsedStorageDTO> basicUsedStorages = usedItemTable.getAmounts();
//		UsedItem[] usedItems = new UsedItem[basicUsedStorages.size()];
//		for(int i=0; i<usedItems.length; i++) {
//			BasicUsedStorageDTO basicUsedStorage = basicUsedStorages.get(i);
//			usedItems[i] = new UsedItem();
//			usedItems[i].setId(basicUsedStorage.getId());
//			usedItems[i].setVersion(basicUsedStorage.getVersion());
//			usedItems[i].setNumberUsedUnits(basicUsedStorage.getAmount());
//			Storage storage = new Storage();
//			storage.setId(basicUsedStorage.getStorageId());
//			storage.setVersion(basicUsedStorage.getStorageVersion());
//			usedItems[i].setStorage(storage);
//			
//			
//		}
//		setUsedItems(usedItems);
//	}


}
