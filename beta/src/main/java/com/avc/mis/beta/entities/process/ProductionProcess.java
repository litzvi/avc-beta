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
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
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
public class ProductionProcess extends ProcessEntity {	

	//cascade remove for testing 
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
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@BatchSize(size = BaseEntity.BATCH_SIZE)
	private Set<ProcessItem> processItems = new HashSet<>();
		
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ApprovalTask> approvals = new HashSet<>();
		
	@OneToMany(mappedBy = "process", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<UserMessage> messages = new HashSet<>();
	
	/**
	 * Gets the list of Items as an array (can be ordered).
	 * @return the processItems
	 */
	public ProcessItem[] getProcessItems() {
		return (ProcessItem[])this.processItems.toArray(new ProcessItem[this.processItems.size()]);
	}

	/**
	 * Setter for adding items that are processed, 
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param processItems the processItems to set
	 */
	public void setProcessItems(ProcessItem[] processItems) {
		this.processItems = Insertable.filterAndSetReference(processItems, (t) -> {t.setReference(this);	return t;});
	}
	
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
		return "Process does not have a recordedTime or process type ";
	}
	
	@PreUpdate
	@Override
	public void preUpdate() {
//		if(!isLegal())
//			throw new IllegalArgumentException(this.getIllegalMessage());
	}
}
