package com.avc.mis.beta.entities.process;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.avc.mis.beta.entities.processinfo.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(ProcessItem[] processItems) {
		super.setProcessItems(processItems);
	}
	
	@NotEmpty(message = "Has to containe at least one destination-storage-item (process item)")
	@Override
	public ProcessItem[] getProcessItems() {
		return super.getProcessItems();
	}
	
	
	
	
//	@JsonIgnore
//	@Override
//	protected boolean canEqual(Object o) {
//		return super.canEqual(o);
//	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Storage transfer";
	}

}
