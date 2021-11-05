/**
 * 
 */
package com.avc.mis.beta.dto.processInfo;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDataDTO;
import com.avc.mis.beta.dto.basic.ProductionLineBasic;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.enums.Shift;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class GeneralProcessInfo extends BasicDataDTO {

	Instant createdDate;
	String userRecording;
	ProcessName processName;
	ProductionLineBasic productionLine;
	LocalDateTime recordedTime;
	Shift shift; 
	LocalTime startTime;
	LocalTime endTime;
	Duration downtime;
	Integer numOfWorkers;
	String personInCharge;
	ProcessStatus processStatus;
	EditStatus editStatus;
	String remarks;
	String[] approvals;

	
	public GeneralProcessInfo(Integer id, Integer version, Instant createdDate, String userRecording, 
			ProcessName processName, Integer productionLineId, String productionLineValue, ProductionFunctionality productionFunctionality,
			LocalDateTime recordedTime, Shift shift, LocalTime startTime, LocalTime endTime, Duration downtime, Integer numOfWorkers, String personInCharge, 
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
		this.shift = shift;
		this.startTime = startTime;
		this.endTime = endTime;
		this.downtime = downtime;
		this.numOfWorkers = numOfWorkers;
		this.personInCharge = personInCharge;
		this.processStatus = processStatus;
		this.editStatus = editStatus;
		this.remarks = remarks;
		if(approvals != null)
			this.approvals = Stream.of(approvals.split(",")).distinct().toArray(String[]::new);
		else
			this.approvals = null;
	}
}
