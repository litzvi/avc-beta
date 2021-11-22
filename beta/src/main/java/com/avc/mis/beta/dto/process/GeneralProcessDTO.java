/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.process.collectionItems.ProcessFileDTO;
import com.avc.mis.beta.dto.process.info.GeneralProcessInfo;
import com.avc.mis.beta.dto.values.ProductionLineDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.Shift;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
	
	//fields excluded from compare for testing, because not set by user.
	@EqualsAndHashCode.Exclude
	private Instant createdDate;
//	private Instant modifiedDate;
	@EqualsAndHashCode.Exclude 
	private String userRecording;
	@EqualsAndHashCode.Exclude 
	private ProcessName processName;
	
	private ProductionLineDTO productionLine;
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
	
	/**
	 * Setting multiple fields of general process for a already constructed object.
	 * Used to fetch and set data with one call from database.
	 * @param info GeneralProcessInfo containing GeneralProcess field values.
	 */
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
			throw new IllegalStateException("Param has to be GeneralProcess class");
		}
		super.fillEntity(generalProcess);

		if(getProductionLine() != null)
			generalProcess.setProductionLine(getProductionLine().fillEntity(new ProductionLine()));
		generalProcess.setRecordedTime(getRecordedTime());
		generalProcess.setShift(getShift());
		generalProcess.setStartTime(getStartTime());
		generalProcess.setEndTime(getEndTime());
		generalProcess.setDowntime(getDowntime());
		generalProcess.setNumOfWorkers(getNumOfWorkers());
		generalProcess.setPersonInCharge(getPersonInCharge());
		generalProcess.setRemarks(getRemarks());
		
		return generalProcess;
	}

	
//	public String getProcessTypeDescription() {
//		return getProcessName().toString();	
//	}
	
//	public abstract String getProcessTypeDescription();

}
