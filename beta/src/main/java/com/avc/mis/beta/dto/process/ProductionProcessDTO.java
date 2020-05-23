/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ContractType;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ProductionProcessDTO extends ProcessDTO {
	
	@EqualsAndHashCode.Exclude // no need to compare for testing
	private Instant createdDate;
//	private Instant modifiedDate;
	@EqualsAndHashCode.Exclude // no need to compare for testing
	private String userRecording; //perhaps only user name
	private PoCodeDTO poCode;
	private ProcessName processName; // use string instead of object or enum
	private ProductionLine productionLine;
	private OffsetDateTime recordedTime;
	private Duration duration;
	private Integer numOfWorkers;
	private ProcessStatus status;
	private String remarks;
	
	public ProductionProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, ContractTypeCode contractTypeCode, Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, Duration duration, Integer numOfWorkers, ProcessStatus status, 
			String remarks) {
		super(id, version);
		this.createdDate = createdDate;
		this.userRecording = userRecording;
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, supplierName);
		this.processName = processName;
		this.productionLine = productionLine;
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.numOfWorkers = numOfWorkers;
		this.status = status;
		this.remarks = remarks;
	}
	
	public ProductionProcessDTO(@NonNull ProductionProcess process) {
		super(process.getId(), process.getVersion());
		this.createdDate = process.getCreatedDate();
		if(process.getCreatedBy() != null)
			this.userRecording = process.getCreatedBy().getUsername();
		this.poCode = new PoCodeDTO(process.getPoCode());
		this.processName = process.getProcessType().getProcessName();
		this.productionLine = process.getProductionLine();
		this.recordedTime = process.getRecordedTime();
		this.duration = process.getDuration();
		this.numOfWorkers = process.getNumOfWorkers();
		this.status = process.getStatus();
		this.remarks = process.getRemarks();
	}
}
