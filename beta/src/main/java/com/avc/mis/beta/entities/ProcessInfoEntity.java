/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import com.avc.mis.beta.entities.process.ProductionProcess;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 * Abstract class for entities representing information about a process entity.
 * e.g. message about a process transaction, task required for process. 
 * References a process (can't be changed) and has a title.
 * Might be changed in the future to not extend ProcessEntity.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ProcessInfoEntity extends ProcessEntity {


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", nullable = false, updatable = false)
	private ProductionProcess process;
	
	private String title;
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.process != null;
	}
	
	@PrePersist
	@Override
	public void prePersist() {
		if(!isLegal() && this.process != null)
			throw new IllegalArgumentException(this.getIllegalMessage());
	}
	
}
