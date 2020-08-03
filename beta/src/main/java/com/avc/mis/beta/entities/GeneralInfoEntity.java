/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for entities representing information notifying about processes -
 * for specific process entity see ProcessInfoEntity class.
 * e.g. message about a process transaction or management info. 
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class GeneralInfoEntity extends AuditedEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", updatable = false)
	private GeneralProcess process;
	
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
