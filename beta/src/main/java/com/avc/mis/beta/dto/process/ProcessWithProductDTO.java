/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class ProcessWithProductDTO<T extends ProcessItemDTO> extends PoProcessDTO {
	

//	@Setter(value = AccessLevel.PUBLIC) @Getter(value = AccessLevel.PROTECTED)
	private List<T> processItems;
	
	
//	public ProcessWithProductDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
//			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
//			Integer supplierId, Integer supplierVersion, String supplierName, String display,
//			ProcessName processName, ProductionLine productionLine, 
//			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
//			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
//		super(id, version, createdDate, userRecording, 
//				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix,
//				supplierId, supplierVersion, supplierName, display,
//				processName, productionLine, recordedTime, startTime, endTime, 
//				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
//	}
	
	
	
	public ProcessWithProductDTO(@NonNull ProcessWithProduct<?> process) {
		super(process);
	}
	
	@Override
	public ProcessWithProduct<? extends ProcessItem> fillEntity(Object entity) {
		ProcessWithProduct<? extends ProcessItem> processWithProduct;
		if(entity instanceof ProcessWithProduct) {
			processWithProduct = (ProcessWithProduct<? extends ProcessItem>) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProcessWithProduct class");
		}
		super.fillEntity(processWithProduct);
		
		return processWithProduct;
	}


}
