package com.avc.mis.beta.dto.process;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.ItemCount;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying StorageTransfer entity data.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class StorageTransferDTO extends TransactionProcessDTO<ProcessItemDTO> {
	

	
//	public StorageTransferDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
//			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
//			Integer supplierId, Integer supplierVersion, String supplierName, String display,
//			ProcessName processName, ProductionLine productionLine, 
//			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
//			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, 
//			String approvals) {
//		super(id, version, createdDate, userRecording, 
//				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix,
//				supplierId, supplierVersion, supplierName, display,
//				processName, productionLine, recordedTime, startTime, endTime, duration, 
//				numOfWorkers, processStatus, editStatus, remarks, approvals);
//	}
	
	
	public StorageTransferDTO(@NonNull StorageTransfer transfer) {
		super(transfer);
		ItemCount[] itemCounts = transfer.getItemCounts();
		if(itemCounts != null)
			this.setItemCounts(Arrays.stream(itemCounts)
					.map(i->{return new ItemCountDTO(i);}).collect(Collectors.toList()));
		super.setProcessItems( Arrays.stream(transfer.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toList()));
		super.setUsedItemGroups(Arrays.stream(transfer.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toList()));

	}
	
	@Override
	public List<ProcessItemDTO> getProcessItems() {
		return super.getProcessItems();
	}
	
	@Override
	public void setProcessItems(List<ProcessItemDTO> processItems) {
		super.setProcessItems(processItems);
	}

	
	@Override
	public List<UsedItemsGroupDTO> getUsedItemGroups() {
		return super.getUsedItemGroups();
	}

	@Override
	public void setUsedItemGroups(List<UsedItemsGroupDTO> usedItemGroups) {
		super.setUsedItemGroups(usedItemGroups);
	}


	
	@Override
	public String getProcessTypeDescription() {
		return "Storage transfer";
	}

}
