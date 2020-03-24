/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntityWithId;
import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESSES")
public class ProductionProcess extends ProcessEntityWithId {
	
	@ManyToOne 
	@JoinColumn(name = "staffId", updatable = false)
	private Staff staffRecording;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "POid")
	private PO po;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, updatable = false)
	private ProcessType processType;
	
	@ManyToOne
	@JoinColumn(name = "productionLineId", updatable = false)
	private ProductionLine productionLine;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime time;
	private Duration duration;//seconds
	private Integer numOfWorkers;
	
	@ManyToOne 
	@JoinColumn(name = "statusId"/*, nullable = false*/)
	private ProcessStatus status;
	private String remarks;
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public boolean isLegal() {
		return this.processType != null;
	}

	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof PO) {
			this.setPo((PO)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a purchase order");
		}		
	}

	@Override
	public String getIllegalMessage() {
		return "Process does not have an inset time or process type";
	}
	
}
