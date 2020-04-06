/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ProductionProcessDTO extends ProcessDTO {
	
	@EqualsAndHashCode.Exclude
	private Instant createdDate;
//	private Instant modifiedDate;
	private UserEntity staffRecording;
	private PoCode poCode;
//	private Integer poId;
	private ProcessType processType;
	private ProductionLine productionLine;
	private LocalDateTime recordedTime;
	private Duration duration;
	private Integer numOfWorkers;
	private ProcessStatus status;
	private String remarks;
	
	public ProductionProcessDTO(Integer id, Long version, Instant createdDate, 
			UserEntity staffRecording, PoCode poCode, ProcessType processType, ProductionLine productionLine, 
			LocalDateTime recordedTime, Duration duration, Integer numOfWorkers, ProcessStatus status, 
			String remarks) {
		super(id, version);
		this.createdDate = createdDate;
		this.staffRecording = staffRecording;
		this.poCode = poCode;
		this.processType = processType;
		this.productionLine = productionLine;
		this.recordedTime = recordedTime;
		this.duration = duration;
		this.numOfWorkers = numOfWorkers;
		this.status = status;
		this.remarks = remarks;
	}
	
	public ProductionProcessDTO(@NonNull ProductionProcess process) {
		super(process.getId(), process.getVersion());
		this.createdDate = process.getCreatedDate();
		this.staffRecording = process.getUser();
		this.poCode = process.getPoCode();
		this.processType = process.getProcessType();
		this.productionLine = process.getProductionLine();
		this.recordedTime = process.getRecordedTime();
		this.duration = process.getDuration();
		this.numOfWorkers = process.getNumOfWorkers();
		this.status = process.getStatus();
		this.remarks = process.getRemarks();
	}
}
