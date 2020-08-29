/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Process of receiving purchased items (with or without an order).
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ORDER_RECEIPTS")
@PrimaryKeyJoinColumn(name = "processId")
public class Receipt extends ProcessWithProduct<ReceiptItem> {
	
	/**
	 * @param receiptItems
	 */
	public void setReceiptItems(ReceiptItem[] receiptItems) {
		super.setProcessItems(receiptItems);
	}
	
	/**
	 * @return array of ReceiptItem in desired order
	 */
	public ReceiptItem[] getReceiptItems() {
		ProcessItem[] processItems = super.getProcessItems();
		return Arrays.copyOf(processItems, processItems.length, ReceiptItem[].class);
	}
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	/**
	 * Life Cycle process status starts with PENDING, since needs to be checked and excepted.
	 */
	@PrePersist
	@Override
	public void prePersist() {
		super.prePersist();
		getLifeCycle().setProcessStatus(ProcessStatus.PENDING);
	
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Receipt";
	}
	
}
