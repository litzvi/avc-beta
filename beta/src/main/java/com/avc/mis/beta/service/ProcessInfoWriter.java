/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.UserMessage;

/**
 * @author Zvi
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessInfoWriter {
	
	@Autowired private ProcessInfoDAO dao;
	

	/**
	 * Approve (or any other decision) to a approval task for a process.
	 * ATTENTION! Should not be used because ApprovalTask user can be changed with current user, 
	 * and will be approved since user isn't updated in the database.
	 * @param approval the approval task with id and user with id.
	 * @param decisionType the decision made.
	 * @throws IllegalArgumentException trying to approve for another user.
	 */
	@Deprecated
	public void approveProcess(ApprovalTask approval, String decisionType) {
		dao.approveProcess(approval, decisionType);
	}
	
	/**
	 * Approve (or any other decision) to a approval task for a process, including snapshot of process state approved.
	 * @param approvalId the ApprovalTask id.
	 * @param decisionType the decision made.
	 * @param processSnapshot snapshot of the process as seen by the approver.
	 * @throws IllegalArgumentException trying to approve for another user.
	 */
	public void setProcessDecision(int approvalId, String decisionType, String processSnapshot) {
		dao.setProcessDecision(approvalId, decisionType, processSnapshot);		
	}
	
	/**
	 * Changes the label of the message. e.g. from NEW to SEEN
	 * @param messageId
	 * @param labelName
	 */
	public void setMessageLabel(int messageId, String labelName) {
		dao.setMessageLabel(messageId, labelName);
	}

	
	
}
