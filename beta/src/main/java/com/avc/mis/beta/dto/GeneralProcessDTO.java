/**
 * 
 */
package com.avc.mis.beta.dto;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.dto.process.collection.CashewItemQualityDTO;
import com.avc.mis.beta.dto.process.collection.ProcessFileDTO;
import com.avc.mis.beta.dto.processInfo.GeneralProcessInfo;
import com.avc.mis.beta.entities.data.ProcessFile;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.Shift;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.values.ProductionLine;

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
	private LocalDateTime recordedTime;

	private Shift shift; 
	private LocalTime startTime;
	private LocalTime endTime;

	private Duration downtime;
	private Integer numOfWorkers;
	
	private String personInCharge;

	@EqualsAndHashCode.Exclude
	private ProcessStatus processStatus;
	@EqualsAndHashCode.Exclude
	private EditStatus editStatus;
	private String remarks;
	@EqualsAndHashCode.Exclude // don't compare for testing
	private String[] approvals;
	
	private List<ProcessFileDTO> processFiles;
		
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
		this.shift = process.getShift();
		this.startTime = process.getStartTime();
		this.endTime = process.getEndTime();
		this.downtime = process.getDowntime();
		this.numOfWorkers = process.getNumOfWorkers();
		this.personInCharge = process.getPersonInCharge();
		this.processStatus = process.getLifeCycle().getProcessStatus();
		this.editStatus = process.getLifeCycle().getEditStatus();
		this.remarks = process.getRemarks();
		this.approvals = process.getApprovals().stream().map(t -> t.getUser().getUsername()).toArray(String[]::new);
		
		if(process.getProcessFiles() != null && !process.getProcessFiles().isEmpty())
			this.processFiles = process.getProcessFiles().stream()
					.map(i->{return new ProcessFileDTO(i);}).collect(Collectors.toList());


	}
	
	public void setGeneralProcessInfo(GeneralProcessInfo info) {
		super.setId(info.getId());
		super.setVersion(info.getVersion());
		this.createdDate = info.getCreatedDate();
		this.userRecording = info.getUserRecording();
		this.processName = info.getProcessName();
		this.productionLine = info.getProductionLine();
		this.recordedTime = info.getRecordedTime();
		this.shift = info.getShift();
		this.startTime = info.getStartTime();
		this.endTime = info.getEndTime();
		this.downtime = info.getDowntime();
		this.numOfWorkers = info.getNumOfWorkers();
		this.personInCharge = info.getPersonInCharge();		
		this.processStatus = info.getProcessStatus();
		this.editStatus = info.getEditStatus();
		this.remarks = info.getRemarks();
		this.approvals = info.getApprovals();
	}
	
	@Override
	public GeneralProcess fillEntity(Object entity) {
		GeneralProcess generalProcess;
		if(entity instanceof GeneralProcess) {
			generalProcess = (GeneralProcess) entity;
		}
		else {
			throw new IllegalArgumentException("Param has to be GeneralProcess class");
		}
		super.fillEntity(generalProcess);
		
//		generalProcess.setCreatedDate(getCreatedDate());
//		generalProcess.setUserRecording(getUserRecording());
//		generalProcess.setProcessType(processType); info.getProcessName();

		if(getProductionLine() != null)
			generalProcess.setProductionLine(getProductionLine().fillEntity(new ProductionLine()));
		generalProcess.setRecordedTime(getRecordedTime());
		generalProcess.setShift(getShift());
		generalProcess.setStartTime(getStartTime());
		generalProcess.setEndTime(getEndTime());
		generalProcess.setDowntime(getDowntime());
		generalProcess.setNumOfWorkers(getNumOfWorkers());
		generalProcess.setPersonInCharge(getPersonInCharge());
//		generalProcess.processStatus = info.getProcessStatus();
//		generalProcess.editStatus = info.getEditStatus();
		generalProcess.setRemarks(getRemarks());
//		generalProcess.approvals = info.getApprovals();
		
		return generalProcess;
	}

	
	public abstract String getProcessTypeDescription();

}
