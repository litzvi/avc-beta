/**
 * 
 */
package com.avc.mis.beta.entities.process;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.ProcessInfoEntity;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.validation.groups.OnPersist;

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
	@NotNull(message = "Process life cycle record status is mandatory", groups = OnPersist.class)
	private RecordStatus status = RecordStatus.EDITABLE;
	
}
