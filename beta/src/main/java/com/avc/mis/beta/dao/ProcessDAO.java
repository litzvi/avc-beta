/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

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
public abstract class ProcessDAO extends DAO {
	
	void addProcessEntity(ProductionProcess process) {
		getEntityManager().persist(process);
		addAlerts(process);
	}
	
	void editProcessEntity(ProductionProcess process) {
		process.setModifiedDate(null);
		editEntity(process);
		updateAlerts(process);
	}
	
	private void addAlerts(ProductionProcess process) {
		
		List<ProcessTypeAlert> alerts = getProcessRepository().findProcessTypeAlerts(process.getProcessType());
		for(ProcessTypeAlert a: alerts) {
			ApprovalTask processApproval = new ApprovalTask();
			processApproval.setProcess(process);
			switch(a.getApprovalType()) {
			case REQUIRED_APPROVAL:
				processApproval.setUser(a.getUser());
//				processApproval.setProcessVersion(process.getVersion());
				processApproval.setTitle("Process added");
				getEntityManager().persist(processApproval);
			case REVIEW:
				addMessage(a.getUser(), process, "New process added");
				break;
			}
		}
	}
	
	private void updateAlerts(ProductionProcess process) {
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
	
	private void addMessage(UserEntity user, ProductionProcess process, String title) {
		UserMessage userMessage = new UserMessage();
		userMessage.setProcess(process);
		userMessage.setUser(user);
		userMessage.setTitle(title);
		userMessage.setLabel(MessageLabel.NEW);
		getEntityManager().persist(userMessage);	
	}
}
