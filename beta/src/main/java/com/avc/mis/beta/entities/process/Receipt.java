/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Process of receiving purchased items (with or without an order).
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "ORDER_RECEIPTS")
@PrimaryKeyJoinColumn(name = "processId")
public class Receipt extends ProcessWithProduct<ReceiptItem> {
	
	/**
	 * @param receiptItems
	 */
	public void setReceiptItems(Set<ReceiptItem> receiptItems) {
		super.setProcessItems(receiptItems);
	}
	
	/**
	 * @return array of ReceiptItem in desired order
	 */
	@NotEmpty(message = "Has to containe at least one received item")
	public Set<ReceiptItem> getReceiptItems() {
		Set<ProcessItem> processItems = super.getProcessItems();
		return processItems.stream().map(i -> (ReceiptItem)i).collect(Collectors.toSet());
//		return Arrays.copyOf(processItems, processItems.length, ReceiptItem[].class);
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
		
	@NotNull(message = "Receipt has to reference a po code")
	@Override
	public BasePoCode getPoCode() {
		return super.getPoCode();
	}
	
}
