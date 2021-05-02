/**
 * 
 */
package com.avc.mis.beta.dao;

import java.math.RoundingMode;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.basic.ShipmentCodeBasic;
import com.avc.mis.beta.dto.query.ItemAmountWithPoCode;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.query.UsedProcess;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.enums.SequenceIdentifier;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.ProcessParent;
import com.avc.mis.beta.entities.process.collection.StorageMovesGroup;
import com.avc.mis.beta.entities.process.collection.UserMessage;
import com.avc.mis.beta.entities.process.collection.WeightedPo;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.ObjectTablesRepository;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;
import com.avc.mis.beta.utilities.ProgramSequence;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

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
	
	/**
	 * Adding (persisting) a PoProcess. 
	 * Adds the process and adds required notifications.
	 * @param process PoProcess to be added.
	 */
	public void addPoProcessEntity(PoProcess process) {
		addGeneralProcessEntity(process);			
	}
	
	/**
	 * Adding (persisting) a transaction process (may have used and stored items). 
	 * Adds the process and adds required notifications.
	 * @param process GeneralProcess to be added.
	 */
	public void addTransactionProcessEntity(TransactionProcess<?> process) {
		addPoProcessEntity(process);
//		//check used items amounts () don't exceed the storage amounts
//		if(!isUsedInventorySufficiant(process.getId())) {
//			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
//		}	
//		setPoWeights(process);
//		setUsedProcesses(process);
	}
	
	public void checkUsedInventoryAvailability(TransactionProcess<?> process) {
		List<StorageBalance> storageBalances = getInventoryRepository().findUsedStorageBalances(process.getId());
		if(storageBalances != null && storageBalances.stream().anyMatch(b -> !b.isLegal())) {
			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
		}
	}
	
	public void checkUsedInventoryAvailability(StorageRelocation process) {
		List<StorageBalance> storageBalances = getInventoryRepository().findRelocationUseBalances(process.getId());
		if(storageBalances != null && storageBalances.stream().anyMatch(b -> !b.isLegal())) {
			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
		}
	}

	/**
	 * Checks if for given storageBalances, the used/moved item storages total don't exceed storage amounts.
	 * Only applies for TransactionProcess or StorageRelocation.
	 * @param storageBalances 
	 * @return true if for all process used storages, used amounts are equal or less than storage amount, false otherwise
	 */
//	private boolean isUsedInventorySufficiant(List<StorageBalance> storageBalances) {		
//		if(storageBalances == null || storageBalances.isEmpty()) {
//			return true;
//		}
//		return storageBalances.stream().allMatch(b -> b.isLegal());
//	}

	/**
	 * Adding (persisting) a Storage Relocation process. 
	 * Should be unified with addTransactionProcessEntity when StorageRelocation will extend TransactionProcess.
	 * @param relocation
	 */
//	public void addStorageRelocationProcessEntity(StorageRelocation relocation) {
//		addPoProcessEntity(relocation);
//		//check used items amounts () don't exceed the storage amounts
//		if(!isUsedInventorySufficiant(relocation.getId())) {
//			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
//		}	
//		setPoWeights(relocation);
//		setUsedProcesses(relocation);
//	}
	
	/**
	 * Adding (persisting) a general transaction process (may have used and stored items). 
	 * Needs a separate process because setPoWeights for items with no weight won't work.
	 * If setPoWeights will be adjusted addGeneralTransactionProcessEntity and addTransactionProcessEntity should be unified.
	 * Adds the process and adds required notifications.
	 * @param process GeneralProcess to be added.
	 */
//	public void addGeneralTransactionProcessEntity(TransactionProcess<?> process) {
//		addPoProcessEntity(process);
//		//check used items amounts () don't exceed the storage amounts
//		if(!isUsedInventorySufficiant(process.getId())) {
//			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
//		}	
//		setUsedProcesses(process);
//	}
		
	public void setUsedProcesses(StorageRelocation process) {
		removeOldProcessParents(process.getId());
		List<UsedProcess> usedProcesses = getProcessRepository().findRelocationUsedProcess(process.getId());
		addUsedProcesses(usedProcesses, process);
	}
	
	public void setUsedProcesses(TransactionProcess<?> process) {
		removeOldProcessParents(process.getId());
		List<UsedProcess> usedProcesses = getProcessRepository().findTransactionUsedProcess(process.getId());
		addUsedProcesses(usedProcesses, process);
	}	
	
	private void addUsedProcesses(List<UsedProcess> usedProcesses, PoProcess process) {
		if(usedProcesses != null && !usedProcesses.isEmpty()) {
			int ordinal = 0;
			for(UsedProcess usedProcess: usedProcesses) {
				ProcessParent processParent = usedProcess.getProcessParent();
				processParent.setOrdinal(ordinal++);
				addEntity(processParent, process);
			}
		}
	}
	
	public List<ItemAmountWithPoCode> setPoWeights(StorageRelocation process) {
		removeOldWeightedPos(process.getId());
		List<ItemAmountWithPoCode> poWeights = getProcessRepository().findRelocationWeightedPos(process.getId());
		addPoWeights(poWeights, process, true);
		return poWeights;
	}	
	
	public void setPoWeights(TransactionProcess<?> process) {
		removeOldWeightedPos(process.getId());
		List<ItemAmountWithPoCode> poWeights = getProcessRepository().findTransactionWeightedPos(process.getId(), ItemGroup.PRODUCT);
		addPoWeights(poWeights, process, true);		
	}
	
	public void setGeneralPoWeights(TransactionProcess<?> process) {
		removeOldWeightedPos(process.getId());
		List<ItemAmountWithPoCode> poWeights = getProcessRepository().findTransactionWeightedPos(process.getId(), ItemGroup.GENERAL);
		addPoWeights(poWeights, process, false);		
	}
	
	private void addPoWeights(List<ItemAmountWithPoCode> poWeights, PoProcess process, boolean setWeight) {
		if(poWeights != null && !poWeights.isEmpty()) {
			AmountWithUnit usedWeight = null;
			if(setWeight) {
				usedWeight = poWeights.stream().map(i -> i.getWeightAmount()).reduce(AmountWithUnit::add).get();
			}
			Map<PoCodeBasic, Optional<AmountWithUnit>> poMap = poWeights.stream()
					.collect(Collectors.groupingBy(ItemAmountWithPoCode::getPoCode, 
							Collectors.mapping(ItemAmountWithPoCode::getWeightAmount, Collectors.reducing(AmountWithUnit::add))));
			int ordinal = 0;
			for(PoCodeBasic poCode: poMap.keySet()) {
				WeightedPo weightedPo = ItemAmountWithPoCode.getWeightedPo(poCode.getId());
				if(usedWeight != null) {
					weightedPo.setWeight(
							poMap.get(poCode).get()
	//						poWeight.getWeightAmount()
							.divide(usedWeight)
							.setScale(MeasureUnit.DIVISION_SCALE, RoundingMode.HALF_EVEN));
				}
				weightedPo.setOrdinal(ordinal++);
				addEntity(weightedPo, process);
			}
		}
	}
	
	private void removeOldWeightedPos(Integer processId) {
		List<WeightedPo> oldWeightedPos = getProcessRepository().findWeightedPoReferences(processId);
		for(WeightedPo weightedPo: oldWeightedPos) {
			getEntityManager().remove(weightedPo);
		}
	}
	
	private void removeOldProcessParents(Integer processId) {
		List<ProcessParent> oldProcessParents = getProcessRepository().findProcessParentReferences(processId);
		for(ProcessParent processParent: oldProcessParents) {
			getEntityManager().remove(processParent);
		}
	}
		
	/**
	 * editing (merging) a process or process information. 
	 * Edits the process and adds required notifications.
	 * @param process GeneralProcess to be edited.
	 */
	public <T extends GeneralProcess> void editGeneralProcessEntity(T process) {
		ProcessLifeCycle lifeCycle = getProcessRepository().findProcessEditStatus(process.getId());
		EditStatus status = lifeCycle.getEditStatus();
		if(status != EditStatus.EDITABLE) {
			throw new AccessControlException("Process was closed for edit");
		}
		editEntity(process);
		editAlerts(process);
	}
	
	public void editPoProcessEntity(PoProcess process) {
		editGeneralProcessEntity(process);			
	}
	
	/**
	 * editing (merging) a transaction process or process information(may have used and stored items). 
	 * Edits the process and adds required notifications.
	 * @param process GeneralProcess to be edited.
	 */
	public <T extends TransactionProcess<?>> void editTransactionProcessEntity(T process) {
		editPoProcessEntity(process);
	}

//	public void editStorageRelocationProcessEntity(StorageRelocation relocation) {
//		
//		checkRemovingUsedProduct(relocation);
//		editPoProcessEntity(relocation);
//		
//		checkUsedInventoryAvailability(relocation);
//		setUsedProcesses(relocation);
//		List<ItemAmountWithPoCode> usedPos = setPoWeights(relocation);
//		checkDAGmaintained(usedPos, relocation.getId());
//		
//	}
	
//	public <T extends GeneralProcess> void editGeneralTransactionProcessEntity(TransactionProcess<?> process) {
//		editGeneralProcessEntity(process);
//
//		checkUsedInventoryAvailability(process);
//		setUsedProcesses(process);
//	}
	
//	public <T extends ProcessWithProduct<?>> void editProcessWithProductEntity(T process) {
//		//TODO check if can change number of units, if balance after change is legal
//		checkRemovingUsedProduct(process);
//		
//		editPoProcessEntity(process);	
//
//		checkProducedInventorySufficiency(process);	
//	}
	
	public <T extends ProcessWithProduct<?>> void checkProducedInventorySufficiency(T process) {
		Stream<StorageBalance> storageBalances = getInventoryRepository().findProducedStorageBalances(process.getId());		
		if(storageBalances.anyMatch(b -> !b.isLegal())) {
			throw new IllegalArgumentException("Process produced amounts can't be reduced because already in use");
		}
	}
	
	public void checkProducedInventorySufficiency(StorageRelocation relocation) {
		Stream<StorageBalance> storageBalances = getInventoryRepository().findStorageMoveBalances(relocation.getId());
		if(!storageBalances.allMatch(b -> b.isLegal())) {
			throw new IllegalArgumentException("Process moved amounts can't be reduced because already in use");
		}	
	}

//	private boolean isProducedInventorySufficiant(Integer processId) {		
//		Stream<StorageBalance> storageBalances = getInventoryRepository().findProducedStorageBalances(processId);
//		return storageBalances.allMatch(b -> b.isLegal());
//	}
	
	public void checkRemovingUsedProduct(StorageRelocation relocation) {
		HashSet<Integer> storageIds = new HashSet<Integer>();
		for(StorageMovesGroup pi: CollectionItemWithGroup.safeCollection(Arrays.asList(relocation.getStorageMovesGroups()))) {
			storageIds.addAll(Arrays.stream(pi.getStorageMoves()).map(StorageBase::getId).collect(Collectors.toSet()));
		}
		if(getProcessRepository().isRelocationRemovingUsedProduct(relocation.getId(), storageIds)) {
			throw new AccessControlException("Process items can't be edited because they are already in use");
		}		
	}
	
	public <T extends ProcessWithProduct<?>> void checkRemovingUsedProduct(T process) {
		HashSet<Integer> storageIds = new HashSet<Integer>();
		for(ProcessItem pi: CollectionItemWithGroup.safeCollection(Arrays.asList(process.getProcessItems()))) {
			storageIds.addAll(Arrays.stream(pi.getStorageForms()).map(Storage::getId).collect(Collectors.toSet()));
		}
		if(getProcessRepository().isRemovingUsedProduct(process.getId(), storageIds)) {
			throw new AccessControlException("Process items can't be edited because they are already in use");
		}	
	}
	
	//call for edit of a TransactionProcess or StorageRelocation
	public void checkDAGmaintained(List<ItemAmountWithPoCode> usedPos, Integer processId) {
		getProcessDescendants(usedPos.stream()
				.map(i -> i.getPoCode().getId()).collect(Collectors.toSet())
				.stream().toArray(Integer[]::new), processId);
	}
	
	public Integer[] getProcessDescendants (Integer[] poCodeIds, @NonNull Integer processId) {
		List<Integer[]> processVertices = getProcessRepository().findTransactionProcessVertices(poCodeIds);
		Map<Integer, List<Integer>> map = processVertices.stream().collect(Collectors.groupingBy(i -> i[0], Collectors.mapping(i -> i[1], Collectors.toList())));
		List<Integer> addedProcesses = null;
		Set<Integer> processDescendants = new HashSet<>();
		processDescendants.add(processId);
		int numDescendants;
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
				int numToAdd = addedProcesses.size();
				numDescendants = processDescendants.size();
				processDescendants.addAll(addedProcesses);
				if(processDescendants.size() != (numDescendants + numToAdd)) {
					throw new IllegalArgumentException("Process using it's descendant");
				}
			}
		} while(addedProcesses != null && !addedProcesses.isEmpty());
		
		return processDescendants.toArray(new Integer[processDescendants.size()]);
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
				processApproval.setDecision(DecisionType.NOT_ATTENDED);
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
	public boolean isPoCodeFree(Integer poCodeId) {
		List<PoCodeBasic> poCodes = getObjectTablesRepository().findFreePoCodes(poCodeId);
		if(poCodes == null || poCodes.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean isShippingCodeFree(Integer shipmentCodeId) {
		List<ShipmentCodeBasic> shipmentCodes = getObjectTablesRepository().findFreeShipmentCodes(shipmentCodeId);
		if(shipmentCodes == null || shipmentCodes.size() == 0) {
			return false;
		}
		return true;
	}
	
	public boolean isPoCodeReceived(Integer poCodeId) {
		return getObjectTablesRepository().isPoCodeReceived(poCodeId);
	}

	public ProgramSequence getSequnce(SequenceIdentifier sequenceIdentifier) {
		return getObjectTablesRepository().findSequence(sequenceIdentifier);
	}

}
