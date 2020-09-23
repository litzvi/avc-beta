/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.Duration;
import java.time.LocalTime;
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
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;
import com.avc.mis.beta.entities.processinfo.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * Entity that holds core information recoded for every process:
 * PO code, 
 * type of process (Cashew order, receive order, roasting etc.),
 * production line (if have multiple), user recorded time, duration,
 * number of workers and process life cycle.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESSES")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class GeneralProcess extends AuditedEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId", nullable = false, updatable = false)
	@NotNull(message = "Internal error: no process type set", groups = OnPersist.class)
	private ProcessType processType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productionLineId")
	private ProductionLine productionLine;
	
	@Column(nullable = false)
	@NotNull(message = "process recorded date and time is mandetory")
	private OffsetDateTime recordedTime;
	
	private LocalTime startTime;
	private LocalTime endTime;
	private Duration duration;
	private Integer numOfWorkers;
	
	@OneToOne(mappedBy = "process", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.LAZY, optional = false)
	private ProcessLifeCycle lifeCycle;
	
	
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ApprovalTask> approvals = new HashSet<>();
		
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<UserMessage> messages = new HashSet<>();
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	@JsonIgnore
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
		
	/**
	 * Creates the life cycle information
	 */
	@PrePersist
	public void prePersist() {
		this.lifeCycle = new ProcessLifeCycle();
		this.lifeCycle.setProcessStatus(ProcessStatus.FINAL);
		lifeCycle.setReference(this);
	}

	/**
	 * @return a description for objects of this class.
	 */
	//TODO: may be static if no specific information added
	public abstract String getProcessTypeDescription() ;
}
