/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.RecordStatus;
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
@Table(name = "PROCESS_LIFE_CYCLE")
public class ProcessLifeCycle extends ProcessInfoEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private RecordStatus status = RecordStatus.EDITABLE;
	
	@JsonIgnore
	@Override
	public boolean isLegal() {
		return super.isLegal() && this.status != null;
	}

	@JsonIgnore
	@Override
	public String getIllegalMessage() {
		return "Process life cycle needs to reference a process and have a record status";
	}

}
