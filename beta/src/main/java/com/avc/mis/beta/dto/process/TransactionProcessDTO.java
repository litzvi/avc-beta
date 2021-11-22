/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.dto.process.group.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.process.group.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Abstract DTO for processes that use up inventory items and add/produce items to inventory.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class TransactionProcessDTO<T extends ProcessItemDTO> extends ProcessWithProductDTO<T> {
	
	private List<UsedItemsGroupDTO> usedItemGroups;
	
	@EqualsAndHashCode.Exclude
	private List<ProcessItemInventory> availableInventory;
		
	public List<AmountWithUnit> getTotalWeight() {
		if(usedItemGroups != null) {			
			Optional<AmountWithUnit> totalAmount = usedItemGroups.stream()
				.filter(i -> i != null)
				.map(i -> i.getTotalWeight())
				.filter(i -> i != null)
				.reduce(AmountWithUnit::add);
			if(totalAmount.isPresent()) {
				return Arrays.asList(totalAmount.get());
			}
		}
		return null;
	}
	
	@Override
	public TransactionProcess<? extends ProcessItem> fillEntity(Object entity) {
		TransactionProcess<? extends ProcessItem> transactionProcess;
		if(entity instanceof TransactionProcess) {
			transactionProcess = (TransactionProcess<? extends ProcessItem>) entity;
		}
		else {
			throw new IllegalStateException("Param has to be TransactionProcess class");
		}
		super.fillEntity(transactionProcess);	
		
		return transactionProcess;
	}
	
	
}
