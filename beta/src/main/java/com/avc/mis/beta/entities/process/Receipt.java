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
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "ORDER_RECEIPTS")
@PrimaryKeyJoinColumn(name = "processId")
public class Receipt extends GeneralProcess {
	
	public void setReceiptItems(ReceiptItem[] receiptItems) {
		super.setProcessItems(receiptItems);
	}
	
	public ReceiptItem[] getReceiptItems() {
		ProcessItem[] processItems = getProcessItems();
		return Arrays.copyOf(processItems, processItems.length, ReceiptItem[].class);
	}
	
	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return super.canEqual(o);
	}
	
	@PrePersist
	@Override
	public void prePersist() {
		super.prePersist();
		//perhaps general order should be final
		getLifeCycle().setProcessStatus(ProcessStatus.PENDING);
		if(getProcessItems().length == 0)
			throw new IllegalArgumentException("Receipt has to containe at least one item line");
		
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Receipt";
	}
	
}
