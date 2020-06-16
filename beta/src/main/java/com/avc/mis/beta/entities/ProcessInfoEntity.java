/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.avc.mis.beta.entities.process.ProductionProcess;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for entities representing information notifying about a process entity.
 * e.g. message about a process transaction, task required for process. 
 * References a process (can't be changed) and has a title.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ProcessInfoEntity extends ProcessEntity {
	
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
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.process != null;
	}
	
}
