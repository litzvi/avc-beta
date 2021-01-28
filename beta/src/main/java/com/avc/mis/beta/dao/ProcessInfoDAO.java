/**
 * 
 */
package com.avc.mis.beta.dao;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.processinfo.UserMessage;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.ObjectTablesRepository;
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
	@Autowired private InventoryRepository inventoryRepository;
	@Autowired private ObjectTablesRepository objectTablesRepository;
	
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
	 * @param process GeneralProcess to be added.
	 */
	public void addGeneralProcessEntity(GeneralProcess process) {
		addEntity(process);
		addAlerts(process);
	}
	
//	public void addPoProcessEntity(PoProcess process) {
//		PoCode poCode = process.getPoCode();
//		if(poCode != null) { 
//			addEntity(process, poCode);
//		}
//		else {
//			addEntity(process);
//		}
//		addAlerts(process);
//	}
	
	/**
	 * Adding (persisting) a transaction process (may have used and stored items). 
	 * Adds the process and adds required notifications.
	 * @param process GeneralProcess to be added.
	 */
	public void addTransactionProcessEntity(TransactionProcess<?> process) {
		addGeneralProcessEntity(process);
		//check used items amounts () don't exceed the storage amounts
		if(!isUsedInventorySufficiant(process.getId())) {
			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
		}
	}
	
//	public void addRelocationProcessEntity(StorageRelocation process) {
//		addGeneralProcessEntity(process);
//		//check used items amounts () don't exceed the storage amounts
//		checkRelocationBalance(process);
//	}
//	
//	public  void editRelocationProcessEntity(StorageRelocation process) {
//		editGeneralProcessEntity(process);
//		checkRelocationBalance(process);
//	}
		
	/**
	 * Checks if for given array used items, used item storages total don't exceed storage amounts.
	 * Checks only for storages that where used by given UsedItems.
	 * @param usedItems array of UsedItem
	 * @return true if for all storages, used amounts are equal or less than storage amount, false otherwise
	 */
	private boolean isUsedInventorySufficiant(Integer processId) {
		
		Stream<StorageBalance> storageBalances = getInventoryRepository().findUsedStorageBalances(processId);
		return storageBalances.allMatch(b -> b.isLegal());
//		Map<Integer, StorageBalance> storageBalanceMap = storageBalances.collect(Collectors.toMap(StorageBalance::getId, o -> o));
//		for(UsedItem i: usedItems) {
//			if(i.getNumberUnits().compareTo(storageBalanceMap.get(i.getStorage().getId()).getBalance()) > 0) {
//				return false;
//			}
//		}
//		
//		return true;
	}
	
	private boolean isProducedInventorySufficiant(Integer processId) {
		
		Stream<StorageBalance> storageBalances = getInventoryRepository().findProducedStorageBalances(processId);
		return storageBalances.allMatch(b -> b.isLegal());
	}

	/**
	 * editing (merging) a process or process information. 
	 * Edits the process and adds required notifications.
	 * @param process GeneralProcess to be edited.
	 */
	public <T extends GeneralProcess> void editGeneralProcessEntity(T process) {
		//check used items amounts don't exceed the storage amounts
		ProcessLifeCycle lifeCycle = getProcessRepository().findProcessEditStatus(process.getId());
		EditStatus status = lifeCycle.getEditStatus();
		if(status != EditStatus.EDITABLE) {
			throw new AccessControlException("Process was closed for edit");
		}
//		ProcessStatus processStatus = lifeCycle.getProcessStatus();
//		if(processStatus == ProcessStatus.CANCELLED) {
//			throw new AccessControlException("Cancelled process can't be edited");
//		}
//		if(getProcessRepository().isProcessReferenced(process.getId())) {
//			throw new AccessControlException("Process can't be edited because it's already in use");
//		}
		process.setModifiedDate(null);
		editEntity(process);

		
		editAlerts(process);

	}
	
	public <T extends ProcessWithProduct<?>> void editProcessWithProductEntity(T process) {
		//TODO check if can change number of units, if balance after change is legal
		HashSet<Integer> storageIds = new HashSet<Integer>();
		for(ProcessItem pi: process.getProcessItems()) {
			storageIds.addAll(Arrays.stream(pi.getStorageForms()).map(Storage::getId).collect(Collectors.toSet()));
		}
		if(getProcessRepository().isRemovingUsedProduct(process.getId(), storageIds)) {
			throw new AccessControlException("Process items can't be edited because they are already in use");
		}
		
		editGeneralProcessEntity(process);	

		if(!isProducedInventorySufficiant(process.getId())) {
			throw new IllegalArgumentException("Process produced amounts can't be reduced because already in use");
		}
	}
	
	/**
	 * editing (merging) a transaction process or process information(may have used and stored items). 
	 * Edits the process and adds required notifications.
	 * @param process GeneralProcess to be edited.
	 */
	public <T extends TransactionProcess<?>> void editTransactionProcessEntity(T process) {
		editProcessWithProductEntity(process);
		//check used items amounts (after edit) don't exceed the storage amounts
		if(!isUsedInventorySufficiant(process.getId())) {
			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
		}
	}
		
	/**
	 * Sets up needed approvals and messages (notifications), for adding a process of the given type.
	 * @param process the new GeneralProcess
	 */
	private void addAlerts(GeneralProcess process) {

		List<ProcessManagement> alerts = getProcessRepository().findProcessTypeAlertsByProcess(process.getId());

		for(ProcessManagement a: alerts) {			
			switch(a.getManagementType()) {
			case APPROVAL:
				ApprovalTask processApproval = new ApprovalTask();
				processApproval.setProcess(process);
				processApproval.setUser(a.getUser());
				processApproval.setDescription("Process added");
				addEntity(processApproval); //user already in the persistence context
			case REVIEW:
				addMessage(a.getUser(), process, "New process added");
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Sets up needed approvals and messages (notifications), for editing the given process.
	 * @param process the edited GeneralProcess
	 */
	private void editAlerts(GeneralProcess process) {
				
		List<ApprovalTask> approvals = getProcessRepository().findProcessApprovals(process.getId());

		for(ApprovalTask approval: approvals) {
			if(approval.getDecision() != DecisionType.NOT_ATTENDED) {
				approval.setDecision(DecisionType.EDIT_NOT_ATTENDED);
			}			
			approval.setDescription("Process added and edited");
		}
		sendMessageAlerts(process, "Old process edited");
		
	}
	
	public void sendMessageAlerts(GeneralProcess process, String title) {
		Set<UserEntity> users = getProcessRepository().findProcessTypeAlertsUsersByProcess(process.getId());
		for(UserEntity user: users) {
			addMessage(user, process, title);
		}
	}
	

	/**
	 * Adds new notifications/messages about a decision made on a process.
	 * @param approval full approval that includes the process, user and decision.
	 */
	private void approvalAlerts(ApprovalTask approval) {
		GeneralProcess process = approval.getProcess();

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
	 * @param process GeneralProcess that's the subject of the message.
	 * @param title the message title
	 */
	public void addMessage(UserEntity user, GeneralProcess process, String title) {
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
	 * Approve (or any other decision) to a approval task for a process, including snapshot of process state approved.
	 * The AprovealTask is implied by the process and user, 
	 * @param processId the process id.
	 * @param decisionType the decision made.
	 * @param processSnapshot snapshot of the process as seen by the approver.
	 * @param remarks
	 * @throws IllegalArgumentException trying to approve for another user.
	 */
	public void setUserProcessDecision(int processId, DecisionType decision, 
			String processSnapshot, String remarks) {
		
		Optional<ApprovalTask> optional = getProcessRepository().findProcessApprovalByProcessAndUser(processId, getCurrentUserId());
		ApprovalTask approval = optional.orElseThrow(() -> new AccessControlException("No approval task for current user"));
		approval.setDecision(decision);
		approval.setProcessSnapshot(processSnapshot);
		approval.setRemarks(remarks);
		editEntity(approval);
		approvalAlerts(approval);
			
	}
	
	/**
	 * Sets the record status for the process life cycle. e.g. FINAL - process items show in inventory
	 * @param processStatus the process state life cycle of the process.
	 * @param processId
	 */
	public void setProcessStatus(ProcessStatus processStatus, Integer processId) {	
		
		Optional<ProcessLifeCycle> optional = 
				getProcessRepository().findProcessLifeCycleManagerByUser(processId, getCurrentUserId());
		ProcessLifeCycle processLifeCycle = optional
			.orElseThrow(() -> new AccessControlException("You don't have permission to manage process life cycle"));
		
		
		if(processLifeCycle.getProcessStatus().compareTo(processStatus)  < 0) {
			
			//check that for all required approvals satisfied if changed to final
			if(processStatus == ProcessStatus.FINAL) {
				List<ApprovalTask> approvals = getProcessRepository().findProcessApprovals(processId);
				if(approvals.stream().anyMatch(a -> a.getDecision() != DecisionType.APPROVED)) {
					throw new IllegalStateException("Can't finalize process before fully approved");
				}				
			}
			
			//check that process items aren't used so can be cancelled
			if(processStatus == ProcessStatus.CANCELLED && 
					(getProcessRepository().isProcessReferenced(processId) || getProcessRepository().isOrderReferenced(processId))) {
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
	 */
	public void setEditStatus(EditStatus editStatus, Integer processId) {	
		
		Optional<ProcessLifeCycle> optional = 
				getProcessRepository().findProcessLifeCycleManagerByUser(processId, getCurrentUserId());
		ProcessLifeCycle processLifeCycle = optional
			.orElseThrow(() -> new AccessControlException("You don't have permission to manage process life cycle"));
				
		//check that process items aren't used so can't be edited -- chnged now can be edited, as long as there is no contradiction
//		if(editStatus == EditStatus.EDITABLE && getProcessRepository().isProcessReferenced(processId)) {
//			throw new IllegalStateException("Can't edit process who's items are used in another process");			
//		}
		
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
//			MessageLabel label = Enum.valueOf(MessageLabel.class, labelName);
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
	public boolean isPoCodeFree(PoCode poCode) {
		List<PoCodeBasic> poCodes = getObjectTablesRepository().findFreePoCodes(poCode.getId());
		if(poCodes == null || poCodes.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Recursive method to get all underlying order pos
	 * @param poCodeId
	 * @return
	 */
	public List<Integer> getOrigionPoCodeIds(Integer poCodeId) {
		List<Integer> poCodeIds = Arrays.asList(poCodeId);
		
		for(int origionPoCode: getObjectTablesRepository().findOrigionPoCodes(poCodeId)) {
			poCodeIds.addAll(getOrigionPoCodeIds(origionPoCode));
		}
		
		return poCodeIds;
	}

	

}
