package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Po Process of moving around po items within the plant. 
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_TRANSFERS")
@PrimaryKeyJoinColumn(name = "processId")
public class StorageTransfer extends TransactionProcess<ProcessItem> {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ItemCount> itemCounts = new HashSet<>();


	/**
	 * Gets the list of Item counts as an array (can be ordered).
	 * @return the itemCounts
	 */
	public ItemCount[] getItemCounts() {
		return this.itemCounts.toArray(new ItemCount[this.itemCounts.size()]);
	}

	/**
	 * Setter for adding item counts, 
	 * receives an array (which can be ordered, for later use to add an order to the item counts).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param itemCounts the itemCounts to set
	 */
	public void setItemCounts(ItemCount[] itemCounts) {
		this.itemCounts = Insertable.setReferences(itemCounts, (t) -> {t.setReference(this);	return t;});
	}
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Storage transfer";
	}

}
