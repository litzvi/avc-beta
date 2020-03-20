/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.ProcessStatus;
import com.avc.mis.beta.entities.process.ProductionLine;
import com.avc.mis.beta.entities.process.ProductionProcess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionProcessDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private Instant insertTime;
	private Staff staffRecording;
	private Integer poId;
	private ProcessType processType;
	private ProductionLine productionLine;
	private LocalDateTime time;
	private Duration duration;
	private Integer numOfWorkers;
	private ProcessStatus status;
	private String remarks;
	
	public ProductionProcessDTO(@NonNull ProductionProcess process) {
		this.id = process.getId();
		this.insertTime = process.getInsertTime();
		this.staffRecording = process.getStaffRecording();
		this.poId = process.getPo().getId();
		this.processType = process.getProcessType();
		this.productionLine = process.getProductionLine();
		this.time = process.getTime();
		this.duration = process.getDuration();
		this.numOfWorkers = process.getNumOfWorkers();
		this.status = process.getStatus();
		this.remarks = process.getRemarks();
	}
}
