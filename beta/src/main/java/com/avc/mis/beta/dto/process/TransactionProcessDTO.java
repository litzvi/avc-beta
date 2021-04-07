/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.entities.process.TransactionProcess;

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
	}
	
	
}
