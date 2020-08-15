/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.UsedItem;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "TRANSACTION_PROCESSES")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class TransactionProcess<T extends ProcessItem> extends PoProcess {

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<ProcessItem> processItems = new HashSet<>();

	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<UsedItem> usedItems = new HashSet<>();

	/**
	 * Gets the list of Items as an array (can be ordered).
	 * @return the processItems
	 */
	protected Object[] getProcessItems() {
		return this.processItems.toArray();
//		return (T[]) this.processItems.toArray(Array.newInstance(c, this.processItems.size()));
	}

	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(T[] processItems) {
		this.processItems = Insertable.setReferences(processItems, (t) -> {t.setReference(this);	return t;});
	}
	

	public UsedItem[] getUsedItems() {
		return this.usedItems.toArray(new UsedItem[this.usedItems.size()]);
	}

	public void setUsedItems(UsedItem[] usedItems) {
		this.usedItems = Insertable.setReferences(usedItems, (t) -> {t.setReference(this);	return t;});
	}

}
