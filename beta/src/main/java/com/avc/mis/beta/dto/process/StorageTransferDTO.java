package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class StorageTransferDTO extends PoProcessDTO {

	private Set<ProcessItemDTO> processItems; //can use a SortedSet like ContactDetails to maintain order
	private Set<UsedItemsGroupDTO> usedItemGroups; //can use a SortedSet like ContactDetails to maintain order
	
	
	public StorageTransferDTO(Integer id, Integer version, Instant createdDate, String userRecording, Integer poCodeId,
			String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	
	public StorageTransferDTO(@NonNull StorageTransfer transfer) {
		super(transfer);
		this.processItems = Arrays.stream(transfer.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toSet());
		this.usedItemGroups = Arrays.stream(transfer.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toSet());
	}
	
	public void setUsedItemGroups(Collection<UsedItemsGroupDTO> usedItemGroups) {
		this.usedItemGroups.addAll(usedItemGroups);
	}
	

	@Override
	public String getProcessTypeDescription() {
		return "Storage transfer";
	}



//	public void setProcessItems(List<ProcessItemWithStorage> processItems) {
//		Map<Integer, List<ProcessItemWithStorage>> map = processItems.stream()
//			.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, Collectors.toList()));
//		this.processItems = new HashSet<>();
//		for(List<ProcessItemWithStorage> list: map.values()) {
//			ProcessItemDTO processItem = list.get(0).getProcessItem();
//			processItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
//			this.processItems.add(processItem);
//		}
//		
//	}
	
	

}
