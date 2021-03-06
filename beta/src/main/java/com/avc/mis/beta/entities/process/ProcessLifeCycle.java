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

import com.avc.mis.beta.entities.GeneralInfoEntity;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.validation.groups.OnPersist;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Owend by a process, in order to follow it's life cycle.
 * e.g. if editable, locked or the confirmation state of the process - pending, final etc.
 * 
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "PROCESS_LIFE_CYCLE")
public class ProcessLifeCycle extends GeneralInfoEntity {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	@NotNull(message = "Process life cycle process status is mandatory", groups = OnPersist.class)
	private ProcessStatus processStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	@NotNull(message = "Process life cycle edit status is mandatory", groups = OnPersist.class)
	private EditStatus editStatus = EditStatus.EDITABLE;

}
