/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.ProcessType;

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
public class ProductionProcess extends BaseEntity {
	
//	@EqualsAndHashCode.Include
//	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@Column(nullable = false, updatable = false)
	private final Instant insertTime;
	
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
	
	public ProductionProcess() {
		this.insertTime = Instant.now();
	}

	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@Override
	public boolean isLegal() {
		return this.insertTime != null && this.processType != null;
	}

	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal()) {
			throw new IllegalStateException(
					"Process does not have an insetr time or process type");
		}
		
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
	
}
