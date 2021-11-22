/**
 * 
 */
package com.avc.mis.beta.entities.process.group;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.process.storages.UsedItem;
import com.avc.mis.beta.entities.process.storages.UsedItemBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class UsedItemsGroup extends ProcessGroup {
	
	{
		setDtype("UsedItemsGroup");
	}
	
	@OneToMany(mappedBy = "group", targetEntity = UsedItemBase.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@NotEmpty(message = "Has to containe at least one used storage item") //probably because persists empty when doing merge on a new one
	private Set<UsedItem> usedItems = new HashSet<>();

	/**
	 * Setter for adding items used in a business/manufacturing process (process input), 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the null objects and sets the needed references to satisfy needed foreign keys of database.
	 * @param usedItems
	 */
	public void setUsedItems(Set<UsedItem> usedItems) {
		this.usedItems = Insertable.setReferences(usedItems, (t) -> {t.setReference(this);	return t;});
	}

}
