/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.dto.basic.UserBasic;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.ProcessName;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * DTO(Data Access Object) for sending or displaying ProcessManagement entity data.
 * 
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ProcessManagementDTO extends LinkDTO {
	
	ProcessName processName;
	UserBasic user;
	ManagementType managementType;
	
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
	
	
}
