/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.process.collection.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
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
	
//	public TransactionProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
//			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
//			Integer supplierId, Integer supplierVersion, String supplierName, String display,
//			ProcessName processName, ProductionLine productionLine, 
//			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
//			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
//		super(id, version, createdDate, userRecording, 
//				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix,
//				supplierId, supplierVersion, supplierName, display,
//				processName, productionLine, recordedTime, startTime, endTime, 
//				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
//	}
	
	
	public TransactionProcessDTO(@NonNull TransactionProcess<?> transaction) {
		super(transaction);
		setUsedItemGroups(transaction.getUsedItemGroups().stream()
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toList()));
	}
	
	public List<AmountWithUnit> getTotalWeight() {
		if(usedItemGroups != null) {			
			Optional<AmountWithUnit> totalAmount = usedItemGroups.stream()
				.filter(i -> i != null)
				.map(i -> i.getTotalWeight())
				.filter(i -> i != null)
				.reduce(AmountWithUnit::add);
			if(totalAmount.isPresent()) {
//				return AmountWithUnit.weightDisplay(totalAmount.get(), Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
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
