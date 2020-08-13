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

import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;
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
//@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessInfoEntity extends AuditedEntity {
	
	@NotNull(message = "System error: Process not referenced", groups = OnPersist.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", updatable = false)
	private ProductionProcess process;
	
	private String description;
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof ProductionProcess) {
			this.setProcess((ProductionProcess)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a production process");
		}		
	}
		
}
