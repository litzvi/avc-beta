/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

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
	private PoProcess process;
	
	private String description;
	
	@Override
	public void setReference(Object referenced) {
		if(referenced instanceof PoProcess) {
			this.setProcess((PoProcess)referenced);
		}
		else {
			throw new ClassCastException("Referenced object isn't a production process");
		}		
	}
		
}
