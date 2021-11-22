/**
 * 
 */
package com.avc.mis.beta.dao;

import java.security.AccessControlException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.link.ProcessManagement;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.system.ApprovalTask;
import com.avc.mis.beta.entities.system.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.ObjectTablesRepository;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.repositories.RelocationRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Data access object class for adding and managing process info.
 * Sending messages, adding approvals, setting approval decisions and checking codes are free to use.
 * 
 * @author Zvi
 *
 */
@Getter(value = AccessLevel.PRIVATE)
@Repository
public abstract class ProcessInfo extends DAO {
	
	@Autowired private ProcessInfoRepository processRepository;
	@Autowired private InventoryRepository inventoryRepository;
	@Autowired private ObjectTablesRepository objectTablesRepository;
	@Autowired private RelocationRepository relocationRepository;
	
	/**
	 * Gets the ProcessType by it's unique name. 
	 * ProcessType is a wrapper for Enum values of ProcessName. 
	 * @param value unique ProcessName for requested ProcessType.
	 * @return ProcessType of the given name.
	 * @throws NullPointerException if ProcessType with given name dosen't exist.
	 */
	public ProcessType getProcessTypeByValue(ProcessName value) {
		return getProcessRepository().findProcessTypeByValue(value)
				.orElseThrow(() -> new NullPointerException("No such process type"));
	}
	
	/**
	 * Gets all IDs of processes who are 'descendants' (using it's product of the given pos) of the given process.
	 * @param poCodeIds looks for descendants using the given pos.
	 * @param processId
	 * @return IDs of 'descendants' of the given process.
	 */
	public Set<Integer> getProcessDescendants(Integer[] poCodeIds, @NonNull Integer processId) {
		List<Integer[]> processVertices = getProcessRepository().findTransactionProcessVertices(poCodeIds);
		Map<Integer, List<Integer>> map = processVertices.stream().collect(Collectors.groupingBy(i -> i[0], Collectors.mapping(i -> i[1], Collectors.toList())));
		List<Integer> addedProcesses = null;
		Set<Integer> processDescendants = new HashSet<>();
		do {
			if(addedProcesses == null) {
				if(map.containsKey(processId))
					addedProcesses = map.get(processId);
			}
			else {
				addedProcesses = addedProcesses.stream()
						.filter(i -> map.containsKey(i))
						.flatMap(i -> map.get(i).stream())
						.collect(Collectors.toList());
			}
			
			if(addedProcesses != null) {
				processDescendants.addAll(addedProcesses);
			}
		} while(addedProcesses != null && !addedProcesses.isEmpty());
		
		return processDescendants;
	}
		
	/**
	 * Sends needed messages (notifications), for adding a process of the given type.
	 * Messages are sent to APPROVAL and REVIEW management types.
	 * @param process the new GeneralProcess
	 * @param title message title
	 */
	protected void sendProcessMessages(GeneralProcess process, String title) {

		List<ProcessManagement> alerts = getProcessRepository().findProcessTypeAlertsByProcess(process.getId());

		for(ProcessManagement a: alerts) {			
			switch(a.getManagementType()) {
			case APPROVAL:
			case REVIEW:
				addMessage(a.getUser(), process, title);
				break;
			default:
				break;
			}
		}
	}
		
	/**
	 * Updates needed approvals and sends messages (notifications), for editing the given process.
	 * Messages are sent to APPROVAL and REVIEW management types.
	 * @param process the edited GeneralProcess
	 */
	protected void editAlerts(GeneralProcess process) {
				
		List<ApprovalTask> approvals = getProcessRepository().findProcessApprovals(process.getId());

		for(ApprovalTask approval: approvals) {
//			NO NEED IF BECAUSE THERE IS NO NOT ATTENDED
//			if(approval.getDecision() != DecisionType.NOT_ATTENDED) {
//				approval.setDecision(DecisionType.EDIT_NOT_ATTENDED);
//			}			
			approval.setDecision(DecisionType.EDIT_NOT_ATTENDED);
			approval.setDescription("Process added and edited");
		}
		sendProcessMessages(process, "Old process edited");
		
	}
	
	/**
	 * Sends message with given title to users with any ManagementType for the given process type.
	 * i.e. sends for APPROVAL, REVIEW and MANAGER
	 * @param processId
	 * @param title message title
	 */
	public void sendMessageAlerts(Integer processId, String title) {
		Set<UserEntity> users = getProcessRepository().findProcessTypeAlertsUsersByProcess(processId);
		for(UserEntity user: users) {
			addMessage(user.getId(), processId, title);
		}
	}
	
	/**
	 * Adds a new message (notification) for a given user about a given process.
	 * @param userId of the recipient of the message.
	 * @param processId Id of process that's the subject of the message.
	 * @param title the message title
	 */
	public void addMessage(Integer userId, Integer processId, String title) {
		GeneralProcess process = null;
		if(processId != null)
			process = getEntityManager().getReference(GeneralProcess.class, processId);
		UserEntity user = getEntityManager().getReference(UserEntity.class, userId);
		addMessage(user, process, title);
	}
		
	/**
	 * Adds a new message (notification) for a given user about a given process.
	 * @param user the recipient of the message, assumes user is already in the persistence context.
	 * @param process GeneralProcess that's the subject of the message.
	 * @param title the message title
	 */
	private void addMessage(UserEntity user, GeneralProcess process, String title) {
		UserMessage userMessage = new UserMessage();
		userMessage.setProcess(process);
		userMessage.setUser(user);
		userMessage.setDescription(title);
		userMessage.setLabel(MessageLabel.NEW);
		addEntity(userMessage);	//user already in the persistence context
	}
		
	/**
	 * Approve (or any other decision) to a approval task for a process, including snapshot of process state approved.
	 * The AprovealTask is implied by the process and user, 
	 * @param processId the process id.
	 * @param decisionType the decision made.
	 * @param processSnapshot snapshot of the process as seen by the approver.
	 * @param remarks
	 * @throws AccessControlException if there is no approval task for current user.
	 */
	public void setUserProcessDecision(int processId, DecisionType decision, 
			String processSnapshot, String remarks) {

		if(getProcessRepository().findProcessManagement(processId, getCurrentUserId(), ManagementType.APPROVAL) == null) {
			throw new AccessControlException("No approval task for given user");
		}

		Optional<Integer> approvalId = getProcessRepository().findProcessApprovalIdByProcessAndUser(processId, getCurrentUserId());
		if(approvalId.isPresent()) {
			ApprovalTask approval = getEntityManager().getReference(ApprovalTask.class, approvalId);
			approval.setDescription("Process " + decision.toString());
			approval.setDecision(decision);
			approval.setProcessSnapshot(processSnapshot);
			approval.setRemarks(remarks);
			editEntity(approval);
			sendProcessMessages(approval.getProcess(), "Process decision: " + approval.getDecision());
		}
		else {
			addApproval(processId, decision, processSnapshot, remarks);
		}
			
	}
	
	/**
	 * Adds a new approval record with the given decision.
	 * @param processId
	 * @param decision
	 * @param processSnapshot
	 * @param remarks
	 */
	private void addApproval(Integer processId, DecisionType decision, String processSnapshot, String remarks) {
		UserEntity user = getEntityManager().getReference(UserEntity.class, getCurrentUserId());
		GeneralProcess process = getEntityManager().getReference(GeneralProcess.class, processId);
		ApprovalTask processApproval = new ApprovalTask();
		processApproval.setProcess(process);
		processApproval.setUser(user);
		processApproval.setDecision(decision);
		processApproval.setProcessSnapshot(processSnapshot);
		processApproval.setDescription("Process " + decision.toString());
		processApproval.setRemarks(remarks);
		addEntity(processApproval); //user already in the persistence context
	}
		
	/**
	 * Sets the record status for the process life cycle. e.g. FINAL - process items show in inventory
	 * @param processStatus the process state life cycle of the process.
	 * @param processId
	 * @throws AccessControlException if current user doesn't have permission to manage process life cycle.
	 * @throws IllegalStateException if trying to finalize not approved process
	 * or trying to cancel process that's referenced by other not cancelled processes
	 * or going back on order of statuses (as ordered in ProcessStatus enum) e.g. from cancelled to final or from final to pending.
	 */
	public void setProcessStatus(ProcessStatus processStatus, Integer processId) {	
		
		Optional<ProcessLifeCycle> optional = 
				getProcessRepository().findProcessLifeCycleManagerByUser(processId, getCurrentUserId());
		ProcessLifeCycle processLifeCycle = optional
			.orElseThrow(() -> new AccessControlException("You don't have permission to manage process life cycle"));
		
		if(processLifeCycle.getProcessStatus().compareTo(processStatus)  < 0) {
			//check that for all required approvals satisfied if changed to final
			if(processStatus == ProcessStatus.FINAL) {
				if(getProcessRepository().waitingApprovals(processId).size() > 0) {
					throw new IllegalStateException("Can't finalize process before fully approved");
				}
			}			
			//check that process items aren't used so can be cancelled
			if(processStatus == ProcessStatus.CANCELLED && 
					(getProcessRepository().isProcessReferenced(processId) || 
							getProcessRepository().isRelocationReferenced(processId) || 
							getProcessRepository().isOrderReferenced(processId))) {
				throw new IllegalStateException("Process can't be cancelled, is referenced by other not cancelled processes");			
			}			
//			processLifeCycle.setStatus(recordStatus); // record status is not updatable for security
			CriteriaUpdate<ProcessLifeCycle> update = 
		    		getEntityManager().getCriteriaBuilder().createCriteriaUpdate(ProcessLifeCycle.class);
		    Root<ProcessLifeCycle> root = update.from(ProcessLifeCycle.class);
		    getEntityManager().createQuery(update.
		    		set("processStatus", processStatus).where(root.get("id").in(processLifeCycle.getId()))).executeUpdate();
		   
		}
		else {
			throw new IllegalArgumentException("Can't change life cycle process status, from: " + 
					processLifeCycle.getProcessStatus() + " to: " + processStatus);
		}
	}
	
	/**
	 * Sets the record status for the process life cycle. e.g. LOCKED - information of the process can't be edited
	 * @param editStatus the editing state life cycle of the process.
	 * @param processId
	 * @throws AccessControlException if current user doesn't have permission to manage process life cycle.
	 */
	public void setEditStatus(EditStatus editStatus, Integer processId) {	
		
		Optional<ProcessLifeCycle> optional = 
				getProcessRepository().findProcessLifeCycleManagerByUser(processId, getCurrentUserId());
		ProcessLifeCycle processLifeCycle = optional
			.orElseThrow(() -> new AccessControlException("You don't have permission to manage process life cycle"));
				
		CriteriaUpdate<ProcessLifeCycle> update = 
	    		getEntityManager().getCriteriaBuilder().createCriteriaUpdate(ProcessLifeCycle.class);
	    Root<ProcessLifeCycle> root = update.from(ProcessLifeCycle.class);
	    getEntityManager().createQuery(update.
	    		set("editStatus", editStatus).where(root.get("id").in(processLifeCycle.getId()))).executeUpdate();		
	}
	
	/**
	 * Changes the label of the message. e.g. from NEW to SEEN
	 * @param messageId
	 * @param labelName
	 */
	public void setMessageLabel(int messageId, MessageLabel label) {
		UserMessage message = getEntityManager().find(UserMessage.class, messageId);
		if(getCurrentUserId().equals(message.getUser().getId())) {//user changes his own message label
			message.setLabel(label);
			editEntity(message);
		}
		else {
			throw new AccessControlException("Can't change message label for another user");
		}
	}

	/**
	 * Checks if po code is free to use for a new order or receipt.
	 * Considered free only if it wasn't used for another order or receipt that weren't cancelled.
	 * @param poCode po code to check
	 * @return true if given PO Code isn't used
	 */
	public <T extends BasePoCode> boolean isPoCodeFree(@NonNull Integer poCodeId, Class<T> clazz) {
		List<PoCodeBasic> poCodes = getObjectTablesRepository().findFreePoCodes(poCodeId, clazz);
		if(poCodes == null || poCodes.size() == 0) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Checks if shipment code is free to use for a container loading.
	 * Meaning it's not previously used by a non canceled process.
	 * @param shipmentCodeId
	 * @return
	 */
	public boolean isShippingCodeFree(Integer shipmentCodeId) {
		List<ShipmentCodeBasic> shipmentCodes = getObjectTablesRepository().findFreeShipmentCodes(shipmentCodeId);
		if(shipmentCodes == null || shipmentCodes.size() == 0) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Sets a Purchase Order (PO) process to closed. 
	 * So it won't show as open even though it wasn't fully received.
	 * @param poId PO process id
	 * @param closed true to close the process
	 * @throws AccessControlException is not a manager for this PO
	 */
	public void closePO(Integer poId, boolean closed) {

		getProcessRepository().findProcessLifeCycleManagerByUser(poId, getCurrentUserId())
			.orElseThrow(() -> new AccessControlException("You don't have permission to close an order"));
		
		CriteriaUpdate<PO> update = 
	    		getEntityManager().getCriteriaBuilder().createCriteriaUpdate(PO.class);
	    Root<PO> root = update.from(PO.class);
	    getEntityManager().createQuery(update.
	    		set("closed", closed).where(root.get("id").in(poId))).executeUpdate();
	}

}
