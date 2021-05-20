/**
 * 
 */
package com.avc.mis.beta.dto.processInfo;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class GeneralProcessInfo extends DataDTO {

	Instant createdDate;
	String userRecording;
	ProcessName processName;
	ProductionLineBasic productionLine;
	LocalDate recordedTime;
	LocalTime startTime;
	LocalTime endTime;
	Duration duration;
	Integer numOfWorkers;
	ProcessStatus processStatus;
	EditStatus editStatus;
	String remarks;
	String approvals;

	
	public GeneralProcessInfo(Integer id, Integer version, Instant createdDate, String userRecording, 
			ProcessName processName, Integer productionLineId, String productionLineValue, ProductionFunctionality productionFunctionality,
			LocalDate recordedTime, LocalTime startTime, LocalTime endTime, Duration duration, Integer numOfWorkers, 
			ProcessStatus processStatus, EditStatus editStatus,
			String remarks, String approvals) {
		super(id, version);
		this.createdDate = createdDate;
		this.userRecording = userRecording;
		this.processName = processName;
		if(productionLineId != null)
			this.productionLine = new ProductionLineBasic(productionLineId, productionLineValue, productionFunctionality);
		else
			this.productionLine = null;
		this.recordedTime = recordedTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		this.numOfWorkers = numOfWorkers;
		this.processStatus = processStatus;
		this.editStatus = editStatus;
		this.remarks = remarks;
		this.approvals = approvals;
	}
}
