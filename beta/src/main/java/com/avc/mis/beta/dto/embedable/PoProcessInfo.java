/**
 * 
 */
package com.avc.mis.beta.dto.embedable;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class PoProcessInfo {

	GeneralProcessInfo generalProcessInfo;
	PoCodeBasic poCode;

	public PoProcessInfo(Integer id, Integer version, Instant createdDate, String staffRecording, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName, String display,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, 
			Duration duration, Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus,
			String remarks , String approvals) {
		this.generalProcessInfo = new GeneralProcessInfo(id, version, createdDate, staffRecording, 
				processName, productionLine, 
				recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, 
				remarks, approvals);
		if(poCodeId != null) {
			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, display);
		}
		else {
			this.poCode = null;
		}
		
	}
}
