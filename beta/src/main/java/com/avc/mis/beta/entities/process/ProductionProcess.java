/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * Entity that holds core information recoded for every process:
 * PO code, 
 * type of process (Cashew order, receive order, roasting etc.),
 * production line (if have multiple), user recorded time, duration,
 * number of workers and the process status(cancelled, finalised etc.)
 * 
 * @author Zvi
 * 
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESSES")
@Inheritance(strategy=InheritanceType.JOINED)
public class ProductionProcess extends ProcessEntity {	

	//cascade remove for testing - need to change to many to one 
	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(updatable = false)
	private PoCode poCode;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId", nullable = false, updatable = false)
	private ProcessType processType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productionLineId", updatable = false)
	private ProductionLine productionLine;
	
	@Column(nullable = false, updatable = false)
	private OffsetDateTime recordedTime;
	private Duration duration;//seconds
	private Integer numOfWorkers;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statusId"/*, nullable = false*/)
	private ProcessStatus status;
	

	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ApprovalTask> approvals = new HashSet<>();
	
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<UserMessage> messages = new HashSet<>();
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.processType != null;
	}

//	@Override
//	public void setReference(Object referenced) {
//		if(referenced instanceof PO) {
//			this.setPo((PO)referenced);
//		}
//		else {
//			throw new ClassCastException("Referenced object isn't a purchase order");
//		}		
//	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process does not have an inset recordedTime or process type";
	}
	
	@PreUpdate
	@Override
	public void preUpdate() {
//		if(!isLegal())
//			throw new IllegalArgumentException(this.getIllegalMessage());
	}
}
