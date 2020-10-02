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
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for entities representing information about a process of a specific po.
 * e.g. process item (only for po process - process for single po), process life cycle etc. 
 * References a po process (required and can't be changed) and has a description.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ProcessInfoEntity extends RankedAuditedEntity {
	
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
