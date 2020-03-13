/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.interfaces.Insertable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PROCESSES")
public class ProductionProcess implements Insertable {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private final Date insertTime;
	
	@ManyToOne 
	@JoinColumn(name = "staffId", updatable = false)
	private Staff staffRecording;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "POid")
	private PO po;
	
	@ManyToOne 
	@JoinColumn(name = "type", nullable = false, updatable = false)
	private ProcessType processType;
	
	@ManyToOne
	@JoinColumn(name = "productionLineId", updatable = false)
	private ProductionLine productionLine;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	private Long duration;//milliseconds
	private Integer numOfWorkers;
	
	@ManyToOne 
	@JoinColumn(name = "statusId"/*, nullable = false*/)
	private ProcessStatus status;
	private String remarks;
	
	public ProductionProcess() {
		this.insertTime = new Date(System.currentTimeMillis());
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
