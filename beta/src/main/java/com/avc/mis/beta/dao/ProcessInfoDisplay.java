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
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ApprovalTask;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Used for accessing and updating information notifying about processes (rather then process data itself). 
 * e.g. messages and approvals.
 * Basically for handling entities who are instance of {@link com.avc.mis.beta.entities.ProcessInfoEntity}
 * 
 * @author Zvi
 *
 */
@Repository
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ProcessInfoDisplay extends DAO {
	
	@Autowired Orders orders;
	
	/**
	 * Get messages for a given user.
	 * @param userId id of the user.
	 * @return List of messages for the given user including the subject process information.
	 */
	public List<UserMessageDTO> getAllMessages(Integer userId) {
		return getProcessRepository().findAllMessagesByUser(userId);
	}
	
	/**
	 * Get new - not read - messages for a given user.
	 * @param userId id of the user.
	 * @return List of new  messages for the given user including the subject process information.
	 */
	public List<UserMessageDTO> getAllNewMessages(Integer userId) {
		return getProcessRepository().findAllMessagesByUserAndLable(userId, new MessageLabel[] {MessageLabel.NEW});
	}
	
	/**
	 * Get all unattended approval tasks required for the given user.
	 * @param userId id of the user.
	 * @return List of unattended approval tasks including the subject process information.
	 */
	public List<ApprovalTaskDTO> getAllRequiredApprovals(Integer userId) {
		return getProcessRepository().findAllRequiredApprovalsByUser(userId, 
				new DecisionType[] {DecisionType.EDIT_NOT_ATTENDED, DecisionType.NOT_ATTENDED});
	}
	
	/**
	 * Get all approval tasks for the given user.
	 * @param userId id of the user.
	 * @return List of approval tasks including the subject process information.
	 */
	public List<ApprovalTaskDTO> getAllApprovals(Integer userId) {
		return getProcessRepository().findAllApprovalsByUser(userId);
	}
	
	/**
	 * Generic method for getting any type of process without needing to find the specific method 
	 * created for that special process type. e.g. order, receiving etc.
	 * @param processId
	 * @param processTypeName
	 * @return ProductionProcessDTO with information of the requested process.
	 */
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

	// if stale process fetched separately
//	public String getProcessApproved(int approvalId) {
//		//TODO
//		return null;
//	}
	
	/**
	 * Approve (or any other decision) to a approval task for a process.
	 * @param approval the approval task with id.
	 * @param decisionType the decision made.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void approveProcess(ApprovalTask approval, String decisionType) {
		DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
		approval.setDecision(decision);
		editEntity(approval);			
	}
	
	/**
	 * Approve (or any other decision) to a approval task for a process, including snapshot of process state approved.
	 * @param approvalId the ApprovalTask id.
	 * @param decisionType the decision made.
	 * @param processSnapshot snapshot of the process as seen by the approver.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void setProcessDecision(int approvalId, String decisionType, String processSnapshot) {
		ApprovalTask approval = getEntityManager().find(ApprovalTask.class, approvalId);
		DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
		approval.setDecision(decision);
		approval.setProcessSnapshot(processSnapshot);
		editEntity(approval);			
	}
}
