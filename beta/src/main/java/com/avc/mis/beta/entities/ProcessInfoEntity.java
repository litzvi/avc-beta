/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for entities representing information notifying about a specific process entity.
 * e.g. task required for process, process item etc. 
 * References a process (required and can't be changed) and has a title.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessInfoEntity extends GeneralInfoEntity {
	
	@NotNull(message = "System error: Process not referenced", groups = OnPersist.class)
	@Override
	public ProductionProcess getProcess() {
		return super.getProcess();
	}
	
}
