/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public abstract class GeneralProcessDTO extends ProcessDTO {
	
	@EqualsAndHashCode.Exclude // no need to compare for testing
	private Instant createdDate;
//	private Instant modifiedDate;
	@EqualsAndHashCode.Exclude // no need to compare for testing
	private String userRecording; //perhaps only user name
	@EqualsAndHashCode.Exclude //if poCode is the same than it's enough, because might be null when testing.
	private ProcessName processName; // use string instead of object or enum
	private ProductionLine productionLine;
	private OffsetDateTime recordedTime;
	private Duration duration;
	private Integer numOfWorkers;
	private ProcessStatus processStatus;
	private EditStatus editStatus;
	private String remarks;
	@EqualsAndHashCode.Exclude // don't compare for testing
	private String approvals;

	
	public GeneralProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
//			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, 
//			Integer supplierId, Integer supplierVersion, String supplierName, 
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, Duration duration, Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus,
			String remarks , String approvals) {
		super(id, version);
		this.createdDate = createdDate;
		this.userRecording = userRecording;
//		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.processName = processName;
		this.productionLine = productionLine;
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.numOfWorkers = numOfWorkers;
		this.processStatus = processStatus;
		this.editStatus = editStatus;
		this.remarks = remarks;
		this.approvals = approvals;

	}
	
	public GeneralProcessDTO(@NonNull GeneralProcess process) {
		super(process.getId(), process.getVersion());
		this.createdDate = process.getCreatedDate();
		if(process.getCreatedBy() != null)
			this.userRecording = process.getCreatedBy().getUsername();
//		this.poCode = new PoCodeDTO(process.getPoCode());
		if(process.getProcessType() != null)
			this.processName = process.getProcessType().getProcessName();
		this.productionLine = process.getProductionLine();
		this.recordedTime = process.getRecordedTime();
		this.duration = process.getDuration();
		this.numOfWorkers = process.getNumOfWorkers();
		this.processStatus = process.getLifeCycle().getProcessStatus();
		this.editStatus = process.getLifeCycle().getEditStatus();
		this.remarks = process.getRemarks();
		this.approvals = process.getApprovals().stream().map(t -> t.getUser().getUsername()).collect(Collectors.joining());

	}
	
	public abstract String getProcessTypeDescription();

}
