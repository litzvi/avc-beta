/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.values.ProductionLine;

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
	
	@Setter(value = AccessLevel.PROTECTED) @Getter(value = AccessLevel.PROTECTED)
	private List<T> processItems;

	public ProcessWithProductDTO(Integer id, Integer version, Instant createdDate, String userRecording, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, 
			Integer supplierId, Integer supplierVersion, String supplierName,
			ProcessName processName, ProductionLine productionLine, 
			OffsetDateTime recordedTime, LocalTime startTime, LocalTime endTime, Duration duration,
			Integer numOfWorkers, ProcessStatus processStatus, EditStatus editStatus, String remarks, String approvals) {
		super(id, version, createdDate, userRecording, 
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix,
				supplierId, supplierVersion, supplierName,
				processName, productionLine, recordedTime, startTime, endTime, 
				duration, numOfWorkers, processStatus, editStatus, remarks, approvals);
	}
	
	
	public ProcessWithProductDTO(@NonNull ProcessWithProduct<?> process) {
		super(process);
	}

}
