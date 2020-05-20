/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.LinkDTO;
import com.avc.mis.beta.dto.values.UserBasic;
import com.avc.mis.beta.entities.data.ProcessAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ApprovalType;
import com.avc.mis.beta.entities.enums.ProcessName;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ProcessAlertDTO extends LinkDTO {
	
	ProcessName processName;
	UserBasic user;
	ApprovalType approvalType;
	
	/**
	 * @param id
	 * @param processType
	 * @param user
	 * @param approvalType
	 */
	public ProcessAlertDTO(Integer id, ProcessName processName, 
			Integer userId, Integer userVersion, String username, 
			ApprovalType approvalType) {
		super(id);
		this.processName = processName;
		this.user = new UserBasic(userId, userVersion, username);
		this.approvalType = approvalType;
	}

	public ProcessAlertDTO(@NonNull ProcessAlert processAlert) {
		super(processAlert.getId());
		this.processName = processAlert.getProcessType().getProcessName();
		UserEntity user = processAlert.getUser();
		this.user = new UserBasic(user.getId(), user.getVersion(), user.getUsername());
		this.approvalType = processAlert.getApprovalType();
	}
	
	
}
