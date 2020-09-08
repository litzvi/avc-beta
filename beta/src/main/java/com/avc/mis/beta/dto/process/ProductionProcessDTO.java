package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
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
public class ProductionProcessDTO extends TransactionProcessDTO {
	
	public ProductionProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, Integer poCodeId,
			String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, OffsetDateTime startTime, OffsetDateTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, poCodeId, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	
	public ProductionProcessDTO(@NonNull ProductionProcess process) {
		super(process);
	}
	

	@Override
	public String getProcessTypeDescription() {
		return getProcessName().toString();	
	}
	

}
