package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.ProductWeightedPoDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.ProductionProcess;
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
public class ProductionProcessDTO extends TransactionProcessDTO<ProcessItemDTO> {
	
	private List<ProductWeightedPoDTO> productWeightedPos;

	
	public ProductionProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, String display,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, 
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName, display,
				processName, productionLine, recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	public ProductionProcessDTO(@NonNull ProductionProcess process) {
		super(process);
		super.setProcessItems( Arrays.stream(process.getProcessItems())
				.map(i->{return new ProcessItemDTO(i);}).collect(Collectors.toList()));
		super.setUsedItemGroups(Arrays.stream(process.getUsedItemGroups())
				.map(i->{return new UsedItemsGroupDTO((UsedItemsGroup)i);}).collect(Collectors.toList()));
		setProductWeightedPos(Arrays.stream(process.getProductWeightedPos())
				.map(i->{return new ProductWeightedPoDTO(i);}).collect(Collectors.toList()));

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
		return getProcessName().toString();	
	}
	
}
