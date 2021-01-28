/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.embedable.PoProcessInfo;
import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying PoProcess entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class PoProcessDTO extends GeneralProcessDTO {
	
	private PoCodeBasic poCode;
	
	private List<ItemCountDTO> itemCounts;
			
//	public PoProcessDTO(Integer id, Integer version, Instant createdDate, String staffRecording, 
//			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
//			Integer supplierId, Integer supplierVersion, String supplierName, String display,
//			ProcessName processName, ProductionLine productionLine, 
//			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, 
//			Duration duration, Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus,
//			String remarks , String approvals) {
//		super(id, version, createdDate, staffRecording, 
//				processName, productionLine, 
//				recordedTime, startTime, endTime, 
//				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
//		if(poCodeId != null)
//			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, display);
//		
//	}
	
	public PoProcessDTO(@NonNull PoProcess process) {
		super(process);
		if(process.getPoCode() != null)
			this.poCode = new PoCodeBasic(process.getPoCode());
	}
	
	public void setPoProcessInfo(PoProcessInfo info) {
		super.setGeneralProcessInfo(info.getGeneralProcessInfo());
		this.poCode = info.getPoCode();
	}

	
}
