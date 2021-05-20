/**
 * 
 */
package com.avc.mis.beta.dto;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.dto.processInfo.GeneralProcessInfo;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.GeneralProcess;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying GeneralProcess entity data.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class GeneralProcessDTO extends DataDTO {
	
	@EqualsAndHashCode.Exclude // no need to compare for testing
	private Instant createdDate;
//	private Instant modifiedDate;
	@EqualsAndHashCode.Exclude // no need to compare for testing
	private String userRecording;
	@EqualsAndHashCode.Exclude //if poCode is the same than it's enough, because might be null when testing.
	private ProcessName processName;
	
	private ProductionLineBasic productionLine;
	private LocalDate recordedTime;

	private LocalTime startTime;
	private LocalTime endTime;

	private Duration duration;
	private Integer numOfWorkers;
	private ProcessStatus processStatus;
	private EditStatus editStatus;
	private String remarks;
	@EqualsAndHashCode.Exclude // don't compare for testing
	private String approvals;
		
//	public GeneralProcessDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
//			ProcessName processName, ProductionLine productionLine, 
//			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration, Integer numOfWorkers, 
//			ProcessStatus processStatus, EditStatus editStatus,
//			String remarks, String approvals) {
//		super(id, version);
//		this.createdDate = createdDate;
//		this.userRecording = userRecording;
//		this.processName = processName;
//		this.productionLine = productionLine;
//		this.recordedTime = recordedTime;
//		this.startTime = startTime;
//		this.endTime = endTime;
//		this.duration = duration;
//		this.numOfWorkers = numOfWorkers;
//		this.processStatus = processStatus;
//		this.editStatus = editStatus;
//		this.remarks = remarks;
//		this.approvals = approvals;
//
//	}
	
	public GeneralProcessDTO(@NonNull GeneralProcess process) {
		super(process.getId(), process.getVersion());
		this.createdDate = process.getCreatedDate();
		if(process.getCreatedBy() != null)
			this.userRecording = process.getCreatedBy().getUsername();
		if(process.getProcessType() != null)
			this.processName = process.getProcessType().getProcessName();
		if(process.getProductionLine() != null)
			this.productionLine = new ProductionLineBasic(process.getProductionLine());
		this.recordedTime = process.getRecordedTime();
		this.startTime = process.getStartTime();
		this.endTime = process.getEndTime();
		this.duration = process.getDuration();
		this.numOfWorkers = process.getNumOfWorkers();
		this.processStatus = process.getLifeCycle().getProcessStatus();
		this.editStatus = process.getLifeCycle().getEditStatus();
		this.remarks = process.getRemarks();
		this.approvals = process.getApprovals().stream().map(t -> t.getUser().getUsername()).collect(Collectors.joining());

	}
	
	public void setGeneralProcessInfo(GeneralProcessInfo info) {
		super.setId(info.getId());
		super.setVersion(info.getVersion());
		this.createdDate = info.getCreatedDate();
		this.userRecording = info.getUserRecording();
		this.processName = info.getProcessName();
		this.productionLine = info.getProductionLine();
		this.recordedTime = info.getRecordedTime();
		this.startTime = info.getStartTime();
		this.endTime = info.getEndTime();
		this.duration = info.getDuration();
		this.numOfWorkers = info.getNumOfWorkers();
		this.processStatus = info.getProcessStatus();
		this.editStatus = info.getEditStatus();
		this.remarks = info.getRemarks();
		this.approvals = info.getApprovals();
	}

	
	public abstract String getProcessTypeDescription();

}
