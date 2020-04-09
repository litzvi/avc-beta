/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ApprovalTask;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(readOnly = true)
public class ProcessDisplay extends DAO {
	
	@Autowired Orders orders;
	
	public List<UserMessageDTO> getAllMessages(Integer userId) {
		return getProcessRepository().findAllMessagesByUser(userId);
	}
	
	public List<ApprovalTaskDTO> getAllRequiredApprovals(Integer userId) {
		return getProcessRepository().findAllRequiredApprovalsByUser(userId, 
				new DecisionType[] {DecisionType.EDIT_NOT_ATTENDED, DecisionType.NOT_ATTENDED});
	}
	
	public List<ApprovalTaskDTO> getAllApprovals(Integer userId) {
		return getProcessRepository().findAllApprovalsByUser(userId);
	}
	
	public ProductionProcessDTO getProcess(int processId, String processTypeName) {
		ProcessName processName = Enum.valueOf(ProcessName.class, processTypeName);
		switch(processName) {
		case CASHEW_ORDER:
		case GENERAL_ORDER:
			return orders.getOrderByProcessId(processId);
			default:
		}
		return null;
	}
	
//	public String getProcessApproved(int approvalId) {
//		//TODO
//		return null;
//	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void approveProcess(ApprovalTask approval, String decisionType) {
		DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
		approval.setDecision(decision);
//		approval.setProcessVersion(processVersion);
		editEntity(approval);			
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void setProcessDecision(int approvalId, String decisionType, String processSnapshot) {
		ApprovalTask approval = getEntityManager().find(ApprovalTask.class, approvalId);
		DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
		approval.setDecision(decision);
		approval.setProcessSnapshot(processSnapshot);
		editEntity(approval);			
	}
}
