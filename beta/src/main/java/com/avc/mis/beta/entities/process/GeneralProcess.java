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
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.avc.mis.beta.validation.groups.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * Entity that holds core information recoded for every process:
 * type of process (Cashew order, receive order, roasting etc.),
 * production line (needed for process types with multiple lines), 
 * user recorded time, start time, end time, duration, number of workers and process life cycle.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
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
	 * Creates and sets the initial life cycle information to FINAL
	 * Should be overridden for process classes who have a different initial state.
	 */
	@PrePersist
	public void prePersist() {
		this.lifeCycle = new ProcessLifeCycle();
		this.lifeCycle.setProcessStatus(ProcessStatus.FINAL);
		lifeCycle.setReference(this);
	}

}
