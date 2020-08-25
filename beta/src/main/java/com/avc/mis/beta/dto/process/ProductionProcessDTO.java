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
import com.avc.mis.beta.entities.process.ProductionProcess;
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
//similar to StorageTransferDTO - perhaps should inherit from this
public class ProductionProcessDTO extends PoProcessDTO {

	private Set<ProcessItemDTO> processItems; //can use a SortedSet like ContactDetails to maintain order
//	private Set<UsedItemDTO> usedItems; //can use a SortedSet like ContactDetails to maintain order
	private Set<UsedItemsGroupDTO> usedItemGroups; //can use a SortedSet like ContactDetails to maintain order
	
	
	public ProductionProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, Integer poCodeId,
			String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, OffsetDateTime recordedTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	
	public ProductionProcessDTO(@NonNull ProductionProcess process) {
		super(process);
		this.processItems = Arrays.stream(process.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toSet());
//		this.usedItems = Arrays.stream(process.getUsedItems())
//				.map(i->{return new UsedItemDTO((UsedItem)i);}).collect(Collectors.toSet());
		this.usedItemGroups = Arrays.stream(process.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toSet());
	}
	
	public void setUsedItemGroups(Collection<UsedItemsGroupDTO> usedItemGroups) {
		this.usedItemGroups.addAll(usedItemGroups);
	}
	

	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}
	

}
