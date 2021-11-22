/**
 * 
 */
package com.avc.mis.beta.dto.link;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.link.ProcessManagement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying ProcessManagement entity data.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcessManagementDTO extends LinkDTO {
	
	private ProcessName processName;
	private UserBasic user;
	private ManagementType managementType;
	
	public ProcessManagementDTO(Integer id, ProcessName processName, 
			Integer userId, Integer userVersion, String username, 
			ManagementType managementType) {
		super(id);
		this.processName = processName;
		this.user = new UserBasic(userId, userVersion, username);
		this.managementType = managementType;
	}

	public ProcessManagementDTO(@NonNull ProcessManagement processAlert) {
		super(processAlert.getId());
		this.processName = processAlert.getProcessType().getProcessName();
		UserEntity user = processAlert.getUser();
		this.user = new UserBasic(user.getId(), user.getVersion(), user.getUsername());
		this.managementType = processAlert.getManagementType();
	}
	
	public BasicValueEntity<ProcessManagement> getApprovalType() {
		return new BasicValueEntity<ProcessManagement>(this.getId(), this.managementType.toString());
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProcessManagement.class;
	}
	
	
}
