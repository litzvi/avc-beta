/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.avc.mis.beta.entities.process.ProductionProcess;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ProcessDataEntity extends ProcessEntity {


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processId", nullable = false)
	private ProductionProcess process;
	
	private String title;
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return this.process != null;
	}

}
