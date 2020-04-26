/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.entities.data.ProcessTypeAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.UserMessage;

/**
 * Abstract class inherited by all process data manipulating services.
 * ensures all process notifications are handled when inserting, editing or removing data about production processes.
 * 
 * @author Zvi
 *
 */
@Transactional(rollbackFor = Throwable.class)
public abstract class ProcessDAO extends DAO {
	
	
	/**
	 * Adding (persisting) a process. 
	 * Adds the process and adds required notifications.
	 * @param process ProductionProcess to be added.
	 */
	void addProcessEntity(ProductionProcess process) {
		addEntity(process);
		addAlerts(process);
	}
	
	/**
	 * editing (merging) a process or process information. 
	 * Edits the process and adds required notifications.
	 * @param process ProductionProcess to be edited.
	 */
	void editProcessEntity(ProductionProcess process) {
		process.setModifiedDate(null);
		editEntity(process);
		editAlerts(process);
	}
	
	
	/**
	 * Sets up needed approvals and messages (notifications), for adding a process of the given type.
	 * @param process the new ProductionProcess
	 */
	private void addAlerts(ProductionProcess process) {
		
		List<ProcessTypeAlert> alerts = getProcessRepository().findProcessTypeAlerts(process.getProcessType());
		for(ProcessTypeAlert a: alerts) {			
			switch(a.getApprovalType()) {
			case REQUIRED_APPROVAL:
				ApprovalTask processApproval = new ApprovalTask();
				processApproval.setProcess(process);
				processApproval.setUser(a.getUser());
				processApproval.setTitle("Process added");
				addEntity(processApproval); //user already in the persistence context
			case REVIEW:
				addMessage(a.getUser(), process, "New process added");
				break;
			}
		}
	}
	
	/**
	 * Sets up needed approvals and messages (notifications), for editing a process of the given type.
	 * @param process the edited ProductionProcess
	 */
	private void editAlerts(ProductionProcess process) {
		List<ApprovalTask> approvals = getProcessRepository().findProcessApprovals(process);
		for(ApprovalTask approval: approvals) {
			if(approval.getDecision() != DecisionType.NOT_ATTENDED) {
				approval.setDecision(DecisionType.EDIT_NOT_ATTENDED);
			}			
			approval.setTitle("Process added and edited");
		}
		
		List<ProcessTypeAlert> alerts = getProcessRepository().findProcessTypeAlerts(process.getProcessType());
		for(ProcessTypeAlert alert: alerts) {
			addMessage(alert.getUser(), process, "Old process edited");
		}
	}
	
	/**
	 * Adds a new message (notification) for a given user about a given process.
	 * @param user the recipient of the message, assumes user is already in the persistence context.
	 * @param process ProductionProcess that's the subject of the message.
	 * @param title the message title
	 */
	private void addMessage(UserEntity user, ProductionProcess process, String title) {
		UserMessage userMessage = new UserMessage();
		userMessage.setProcess(process);
		userMessage.setUser(user);
		userMessage.setTitle(title);
		userMessage.setLabel(MessageLabel.NEW);
		addEntity(userMessage);	//user already in the persistence context
	}
}
