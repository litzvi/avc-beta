/**
 * 
 */
package com.avc.mis.beta.dao;

import java.security.AccessControlException;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;
import com.avc.mis.beta.entities.processinfo.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.repositories.ProcessInfoRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Data access object class used by all process data manipulating services.
 * Provides access process managing data (e.g. ProcessTypes) for services classes,
 * ensures all process notifications are handled when inserting, editing or removing data 
 * of/about production processes.
 * 
 * @author Zvi
 *
 */
@Getter(value = AccessLevel.PRIVATE)
@Repository
public class ProcessInfoDAO extends DAO {
	
	@Autowired private ProcessInfoRepository processRepository;
	
	
	/**
	 * Gets the ProcessType by it's unique name. 
	 * ProcessType is a wrapper for Enum values of ProcessName. 
	 * @param value unique ProcessName for requested ProcessType.
	 * @return ProcessType with the given name.
	 * @throws NullPointerException if ProcessType with given name dosen't exist.
	 */
	public ProcessType getProcessTypeByValue(ProcessName value) {
		return getProcessRepository().findProcessTypeByValue(value)
				.orElseThrow(() -> new NullPointerException("No such process type"));
	}
	
	/**
	 * Adding (persisting) a process. 
	 * Adds the process and adds required notifications.
	 * @param process ProductionProcess to be added.
	 */
	public void addProcessEntity(ProductionProcess process) {
		addEntity(process);
		addAlerts(process);
	}
	
	/**
	 * editing (merging) a process or process information. 
	 * Edits the process and adds required notifications.
	 * @param process ProductionProcess to be edited.
	 */
	public <T extends ProductionProcess> void editProcessEntity(T process) {
		RecordStatus status = getProcessRepository().findProcessLifeCycleStatus(process.getId());
		if(status != RecordStatus.EDITABLE) {
			throw new AccessControlException("Process was closed for edit");
		}
		process.setModifiedDate(null);
		editEntity(process);
		editAlerts(process);
	}
	
	
	/**
	 * Sets up needed approvals and messages (notifications), for adding a process of the given type.
	 * @param process the new ProductionProcess
	 */
	private void addAlerts(ProductionProcess process) {

		List<ProcessManagement> alerts = getProcessRepository().findProcessTypeAlertsByProcess(process.getId());

		for(ProcessManagement a: alerts) {			
			switch(a.getManagementType()) {
			case APPROVAL:
				ApprovalTask processApproval = new ApprovalTask();
				processApproval.setProcess(process);
				processApproval.setUser(a.getUser());
				processApproval.setDescription(process.getProcessType() + " process added");
				addEntity(processApproval); //user already in the persistence context
			case REVIEW:
				addMessage(a.getUser(), process, "New " + process.getProcessTypeDescription() + " process added");
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Sets up needed approvals and messages (notifications), for editing the given process.
	 * @param process the edited ProductionProcess
	 */
	private void editAlerts(ProductionProcess process) {

		List<ApprovalTask> approvals = getProcessRepository().findProcessApprovals(process.getId());

		for(ApprovalTask approval: approvals) {
			if(approval.getDecision() != DecisionType.NOT_ATTENDED) {
				approval.setDecision(DecisionType.EDIT_NOT_ATTENDED);
			}			
			approval.setDescription(process.getProcessType() + "process added and edited");
		}
		
		List<ProcessManagement> alerts = getProcessRepository().findProcessTypeAlertsByProcess(process.getId());
		for(ProcessManagement alert: alerts) {
			addMessage(alert.getUser(), process, "Old " + process.getProcessType() + " process edited");
		}
	}
	

	/**
	 * Adds new notifications/messages about a decision made on a process.
	 * @param approval full approval that includes the process, user and decision.
	 */
	private void approvalAlerts(ApprovalTask approval) {
		ProductionProcess process = approval.getProcess();

		List<ProcessManagement> alerts = getProcessRepository()
				.findProcessTypeAlertsByProcess(process.getId());

		for(ProcessManagement alert: alerts) {
			addMessage(alert.getUser(), process, 
					"Process decision: " + approval.getDecision());
		}
		
	}
	
	/**
	 * Adds a new message (notification) for a given user about a given process.
	 * @param user the recipient of the message, assumes user is already in the persistence context.
	 * @param process ProductionProcess that's the subject of the message.
	 * @param title the message title
	 */
	public void addMessage(UserEntity user, ProductionProcess process, String title) {
		UserMessage userMessage = new UserMessage();
		userMessage.setProcess(process);
		userMessage.setUser(user);
		userMessage.setDescription(title);
		userMessage.setLabel(MessageLabel.NEW);
		addEntity(userMessage);	//user already in the persistence context
	}
	
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
		if(getCurrentUserId().equals(approval.getUser().getId())) {//sign it's own approval
			DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
			approval.setDecision(decision);
			editEntity(approval);
			approvalAlerts(approval);
		}
		else {
			throw new AccessControlException("Can't approve for another user");
		}
	}
	
	/**
	 * Approve (or any other decision) to a approval task for a process, including snapshot of process state approved.
	 * @param approvalId the ApprovalTask id.
	 * @param decisionType the decision made.
	 * @param processSnapshot snapshot of the process as seen by the approver.
	 * @param remarks
	 * @throws IllegalArgumentException trying to approve for another user.
	 */
	public void setProcessDecision(int approvalId, DecisionType decision, 
			String processSnapshot, String remarks) {
		ApprovalTask approval = getEntityManager().getReference(ApprovalTask.class, approvalId);
		if(getCurrentUserId().equals(approval.getUser().getId())) {//sign it's own approval
//			DecisionType decision = Enum.valueOf(DecisionType.class, decisionType);
			approval.setDecision(decision);
			approval.setProcessSnapshot(processSnapshot);
			approval.setRemarks(remarks);
			editEntity(approval);
			approvalAlerts(approval);
		}
		else {
			throw new AccessControlException("Can't approve for another user");
		}		
	}
	
	/**
	 * Sets the record status for the process life cycle. e.g. LOCKED - information of the process can't be edited
	 * @param recordStatus the editing/state life cycle of the process.
	 * @param processId
	 */
	public void setProcessRecordStatus(RecordStatus recordStatus, Integer processId) {	
		
		Optional<ProcessLifeCycle> optional = 
				getProcessRepository().findProcessLifeCycleManagerByUser(processId, getCurrentUserId());
		ProcessLifeCycle processLifeCycle = optional
			.orElseThrow(() -> new AccessControlException("You don't have permission to manage process life cycle"));
		
		
		if(processLifeCycle.getStatus().compareTo(recordStatus)  < 0) {
			
			//check that for all required approvals satisfied if changed to final
			if(recordStatus == RecordStatus.FINAL) {
				List<ApprovalTask> approvals = getProcessRepository().findProcessApprovals(processId);
				if(approvals.stream().anyMatch(a -> a.getDecision() != DecisionType.APPROVED)) {
					throw new IllegalStateException("Can't finalize process before fully approved");
				}				
			}
			
//			processLifeCycle.setStatus(recordStatus); // record status is not updatable for security
			CriteriaUpdate<ProcessLifeCycle> update = 
		    		getEntityManager().getCriteriaBuilder().createCriteriaUpdate(ProcessLifeCycle.class);
		    Root<ProcessLifeCycle> root = update.from(ProcessLifeCycle.class);
		    getEntityManager().createQuery(update.
		    		set("status", recordStatus).where(root.get("id").in(processLifeCycle.getId()))).executeUpdate();
		   
		}
		else {
			throw new IllegalArgumentException("Can't change life cycle status, from: " + 
					processLifeCycle.getStatus() + " to: " + recordStatus);
		}
	}
	
	/**
	 * Changes the label of the message. e.g. from NEW to SEEN
	 * @param messageId
	 * @param labelName
	 */
	public void setMessageLabel(int messageId, MessageLabel label) {
		UserMessage message = getEntityManager().find(UserMessage.class, messageId);
		if(getCurrentUserId().equals(message.getUser().getId())) {//user changes his own message label
//			MessageLabel label = Enum.valueOf(MessageLabel.class, labelName);
			message.setLabel(label);
			editEntity(message);
		}
		else {
			throw new AccessControlException("Can't change message label for another user");
		}
	}

}
