/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import com.avc.mis.beta.entities.data.ProcessTypeAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.data.UserMessage;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProcessApproval;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author Zvi
 *
 */
public abstract class ProcessDAO extends DAO {
	
	void addProcessEntity(PO po) {
		getEntityManager().persist(po);
		addAlerts(po);
	}
	
	void editProcessEntity(ProductionProcess process) {
		process.setModifiedDate(null);
		editEntity(process);
		updateAlerts(process);
	}

	
	void addAlerts(ProductionProcess process) {
		
		List<ProcessTypeAlert> alerts = getProcessRepository().findProcessTypeAlerts(process.getProcessType());
		for(ProcessTypeAlert a: alerts) {
			ProcessApproval processApproval = new ProcessApproval();
			processApproval.setProcess(process);
			switch(a.getApprovalType()) {
			case REQUIRED_APPROVAL:
				processApproval.setUser(a.getUser());
				processApproval.setTitle("Process added");
				getEntityManager().persist(processApproval);
			case REVIEW:
				addMessage(a.getUser(), process, "New process added");
				break;
			}
		}
	}
	
	private void updateAlerts(ProductionProcess process) {
		List<ProcessApproval> approvals = getProcessRepository().findProcessApprovals(process);
		for(ProcessApproval a: approvals) {
			if(a.getDecision() != DecisionType.NOT_ATTENDED) {
				a.setDecision(DecisionType.NOT_ATTENDED);
			}
			a.setTitle("Process edited");
		}
		
		List<ProcessTypeAlert> alerts = getProcessRepository().findProcessTypeAlerts(process.getProcessType());
		for(ProcessTypeAlert a: alerts) {
			addMessage(a.getUser(), process, "Old process edited");
		}
	}
	
	private void addMessage(UserEntity user, ProductionProcess process, String title) {
		UserMessage userMessage = new UserMessage();
		userMessage.setProcess(process);
		userMessage.setUser(user);
		userMessage.setTitle(title);
		getEntityManager().persist(userMessage);	
	}
}
