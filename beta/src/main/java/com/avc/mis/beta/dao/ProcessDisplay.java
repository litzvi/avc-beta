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
import com.avc.mis.beta.entities.process.ProductionProcess;

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
		return getProcessRepository().findAllRequiredApprovalsByUser(userId);
	}
	
	public ProductionProcessDTO getProcess(Integer processId, String processTypeName) {
		ProcessName processName = Enum.valueOf(ProcessName.class, processTypeName);
		switch(processName) {
		case CASHEW_ORDER:
		case GENERAL_ORDER:
			return orders.getOrderByProcessId(processId);
			default:
		}
		return null;
	}
	
	public ProductionProcessDTO getProcess(Integer processId, Long version, String processTypeName) {
		//TODO
		return null;
	}
	
	public void approveProcess(ApprovalTask approval, String decisionType) {
		if(approval != null && approval.getProcessVersion() != null) {
			DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
			approval.setDecision(decision);
			editEntity(approval);	
		}
		else {
			throw new IllegalArgumentException("Approval needs to refer to a certain process version");
		}
			
	}
}
