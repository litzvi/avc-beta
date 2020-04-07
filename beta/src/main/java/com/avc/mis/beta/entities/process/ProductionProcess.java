/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ProcessEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.data.UserMessage;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.values.ProcessStatus;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESSES")
@Inheritance(strategy=InheritanceType.JOINED)
public class ProductionProcess extends ProcessEntity {	

//	@ToString.Exclude
	//cascade remove for testing 
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn(updatable = false)
	private PoCode poCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "typeId", nullable = false, updatable = false)
	private ProcessType processType;
	
	@ManyToOne
	@JoinColumn(name = "productionLineId", updatable = false)
	private ProductionLine productionLine;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime recordedTime;
	private Duration duration;//seconds
	private Integer numOfWorkers;
	
	@ManyToOne 
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
	
}
