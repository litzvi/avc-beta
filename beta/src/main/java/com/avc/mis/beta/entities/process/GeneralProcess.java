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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.processinfo.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
public abstract class GeneralProcess extends AuditedEntity {	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, nullable = false)
	private PoCode poCode;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId", nullable = false, updatable = false)
	@NotNull(message = "Internal error: no process type set", groups = OnPersist.class)
	private ProcessType processType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productionLineId", updatable = false)
	private ProductionLine productionLine;
	
	@Column(nullable = false, updatable = false)
	private OffsetDateTime recordedTime;
	private Duration duration;//seconds
	private Integer numOfWorkers;
	
	@OneToOne(mappedBy = "process", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.LAZY, optional = false)
	private ProcessLifeCycle lifeCycle;
	
	
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ApprovalTask> approvals = new HashSet<>();
		
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<UserMessage> messages = new HashSet<>();
	
	
	
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
//	@PrePersist
//	public abstract void prePersist();
	
	@PrePersist
	public void prePersist() {
		this.lifeCycle = new ProcessLifeCycle();
		this.lifeCycle.setProcessStatus(ProcessStatus.FINAL);
		lifeCycle.setReference(this);
	}

	public abstract String getProcessTypeDescription() ;
	
}
