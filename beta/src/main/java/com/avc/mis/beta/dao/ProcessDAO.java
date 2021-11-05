/**
 * 
 */
package com.avc.mis.beta.dao;

import java.math.RoundingMode;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.process.ProcessWithProductDTO;
import com.avc.mis.beta.dto.process.RelocationProcessDTO;
import com.avc.mis.beta.dto.process.TransactionProcessDTO;
import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.process.collection.StorageMovesGroupDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageMoveDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithPoCode;
import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.query.UsedProcess;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.process.RelocationProcess;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.ProcessParent;
import com.avc.mis.beta.entities.process.collection.WeightedPo;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.ObjectTablesRepository;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.repositories.RelocationRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

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
public class ProcessDAO extends ProcessInfo {
	
	@Autowired private ProcessInfoRepository processRepository;
	@Autowired private InventoryRepository inventoryRepository;
	@Autowired private ObjectTablesRepository objectTablesRepository;
	@Autowired private RelocationRepository relocationRepository;
	
	
	/**
	 * Adding (persisting) a general process.
	 * Adds the process and adds required notifications.
	 * @param process filled GeneralProcessDTO to be added.
	 * @param supplier of entity object.
	 * @return assigned process id.
	 */
	public Integer addGeneralProcessEntity(GeneralProcessDTO process, Supplier<? extends GeneralProcess> supplier) {
		GeneralProcess processEntity = process.fillEntity(supplier.get());
		processEntity.setProcessType(getProcessTypeByValue(process.getProcessName()));
		addEntity(processEntity);		
		sendProcessMessages(processEntity, "New process added");
		
		return processEntity.getId();
	}
	
	/**
	 * Adding (persisting) a po process.
	 * Adds the process and adds required notifications.
	 * @param process filled PoProcessDTO to be added.
	 * @param supplier of entity object.
	 * @return assigned process id.
	 */
	public Integer addPoProcessEntity(PoProcessDTO process, Supplier<? extends PoProcess> supplier) {
		return addGeneralProcessEntity(process, supplier);			
	}
	
	/**
	 * Adding (persisting) a transaction process (may have used and processed items).
	 * Adds the process and adds required notifications.
	 * @param process filled TransactionProcessDTO to be added.
	 * @param supplier of entity object.
	 * @return assigned process id.
	 */
	public Integer addTransactionProcessEntity(TransactionProcessDTO<?> process, Supplier<? extends TransactionProcess<?>> supplier) {
		return addPoProcessEntity(process, supplier);
	}
	
	/**
	 * Adding (persisting) a relocation process.
	 * Adds the process and adds required notifications.
	 * @param process filled RelocationProcessDTO to be added.
	 * @param supplier of entity object.
	 * @return assigned process id.
	 */
	public <T extends RelocationProcessDTO> Integer addRelocationProcessEntity(T process, Supplier<? extends RelocationProcess> supplier) {
		setStorageMovesProcessItem(process.getStorageMovesGroups());
		Integer processId = addPoProcessEntity(process, supplier);
		checkRelocationUsedInventoryAvailability(processId);
		setRelocationPoWeights(processId);
		setRelocationProcessParents(processId);
		checkRelocationBalance(processId);
		return processId;
	}
	
	/**
	 * Editing (merging) general process information. 
	 * Edits the process and adds required notifications.
	 * @param process GeneralProcessDTO to be edited.
	 * @param supplier of entity object.
	 */
	public void editGeneralProcessEntity(GeneralProcessDTO process, Supplier<? extends GeneralProcess> supplier) {
		checkProcessEditablity(process.getId());
		GeneralProcess processEntity = process.fillEntity(supplier.get());
		editEntity(processEntity);
		editAlerts(processEntity);
	}
	
	/**
	 * Editing (merging) po process information. 
	 * Edits the process and adds required notifications.
	 * @param process PoProcessDTO to be edited.
	 * @param supplier of entity object.
	 */
	public void editPoProcessEntity(PoProcessDTO process, Supplier<? extends PoProcess> supplier) {
		editGeneralProcessEntity(process, supplier);			
	}
	
	/**
	 * Editing (merging) a transaction process or process information (may have used and stored items). 
	 * Edits the process and adds required notifications.
	 * @param process GeneralProcessDTO to be edited.
	 * @param supplier of entity object.
	 */
	public <T extends TransactionProcessDTO<?>> void editTransactionProcessEntity(T process, Supplier<? extends TransactionProcess<?>> supplier) {
		editPoProcessEntity(process, supplier);
	}
	
	/**
	 * Editing (merging) relocation process information. 
	 * Edits the process and adds required notifications.
	 * @param process RelocationProcess to be edited.
	 * @param supplier of entity object.
	 */
	public <T extends RelocationProcessDTO> void editRelocationProcessEntity(T process, Supplier<? extends RelocationProcess> supplier) {
		setStorageMovesProcessItem(process.getStorageMovesGroups());
		
		checkRemovingUsedProduct(process);
		
		editPoProcessEntity(process, supplier);
		
		checkRelocationUsedInventoryAvailability(process.getId());
		setRelocationProcessParents(process.getId());
		List<ItemAmountWithPoCode> usedPos = setRelocationPoWeights(process.getId());
		checkDAGmaintained(usedPos, process.getId());

		checkUsingProcesessConsistency(process);
		checkRelocationBalance(process.getId());			
	}
	
	/**
	 * Checks if process is open for edit.
	 * @param processId
	 * @throws AccessControlException if process is closed for edit.
	 */
	public void checkProcessEditablity(Integer processId) {
		ProcessLifeCycle lifeCycle = getProcessRepository().findProcessEditStatus(processId);
		EditStatus status = lifeCycle.getEditStatus();
		if(status != EditStatus.EDITABLE) {
			throw new AccessControlException("Process was closed for edit");
		} 
	}
	
	
	/**
	 * Checks if for given process the used item storages total don't exceed storage amounts.
	 * Only applies for TransactionProcess.
	 * @param transactionProcessId id of the transaction process
	 * @throws IllegalArgumentException if total exceed storage amounts.
	 */
	public void checkTransactionUsedInventoryAvailability(Integer transactionProcessId) {
		List<StorageBalance> storageBalances = getInventoryRepository().findUsedStorageBalances(transactionProcessId);
		if(storageBalances != null && storageBalances.stream().anyMatch(b -> !b.isLegal())) {
			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
		}
	}
	
	/**
	 * Checks if for given process, relocated item storages total don't exceed storage amounts.
	 * Only applies for StorageRelocation.
	 * @param relocationProcessId id of the relocation process
	 * @throws IllegalArgumentException if total exceed storage amounts.
	 */
	public void checkRelocationUsedInventoryAvailability(Integer relocationProcessId) {
		List<StorageBalance> storageBalances = getInventoryRepository().findRelocationUseBalances(relocationProcessId);
		if(storageBalances != null && storageBalances.stream().anyMatch(b -> !b.isLegal())) {
			throw new IllegalArgumentException("Process used item amounts exceed amount in inventory");
		}
	}

	/**
	 * Updates the process's po weights after add or edit of process.
	 * PO weights are the normalized weights of different pos composing the items used in the given process.
	 * Only applies for StorageRelocation.
	 * @param processId
	 * @return List<ItemAmountWithPoCode> of amounts for each po and item used by given process.
	 */
	public List<ItemAmountWithPoCode> setRelocationPoWeights(Integer processId) {
		removeOldWeightedPos(processId);
		List<ItemAmountWithPoCode> poWeights = getProcessRepository().findRelocationWeightedPos(processId);
		addPoWeights(poWeights, processId);
		return poWeights;
	}
	
	/**
	 * Updates the process's po weights after add or edit of process.
	 * PO weights are the normalized weights of different pos composing the items used in the given process.
	 * Only applies for TransactionProcess.
	 * @param processId
	 * @param itemGroups what item groups should be considered for the po weights.
	 */
	public void setTransactionPoWeights(Integer processId, ItemGroup[] itemGroups) {
		removeOldWeightedPos(processId);
		List<ItemAmountWithPoCode> poWeights = 
				getProcessRepository().findTransactionWeightedPos(processId, itemGroups);
		addPoWeights(poWeights, processId);		
	}
	
	/**
	 * Removes all weight pos recordings for the given process.
	 * @param processId
	 */
	private void removeOldWeightedPos(Integer processId) {
		List<WeightedPo> oldWeightedPos = getProcessRepository().findWeightedPoReferences(processId);
		for(WeightedPo weightedPo: oldWeightedPos) {
			getEntityManager().remove(weightedPo);
		}
	}
		
	/**
	 * Records po weights for the given list items with their pos (from the given process)
	 * PO weights are the normalized weights of different pos composing the items used in the given process.
	 * @param poWeights List<ItemAmountWithPoCode> items with their pos (supposed to be from the given process)
	 * @param processId
	 */
	private void addPoWeights(List<ItemAmountWithPoCode> poWeights, Integer processId) {
		if(poWeights != null && !poWeights.isEmpty()) {
			Optional<AmountWithUnit> usedWeight = poWeights.stream().map(i -> i.getWeightAmount()).filter(j -> j != null).reduce(AmountWithUnit::add);
			Map<PoCodeBasic, Optional<AmountWithUnit>> poMap = poWeights.stream()
					.collect(Collectors.groupingBy(ItemAmountWithPoCode::getPoCode, 
							Collectors.mapping(ItemAmountWithPoCode::getWeightAmount, Collectors.reducing(AmountWithUnit::addNullable))));
			int ordinal = 0;
			for(PoCodeBasic poCode: poMap.keySet()) {
				WeightedPo weightedPo = ItemAmountWithPoCode.getWeightedPo(poCode.getId());
				if(usedWeight != null && poMap.get(poCode).isPresent()) {
					weightedPo.setWeight(
							poMap.get(poCode).get()
							.divide(usedWeight.get())
							.setScale(MeasureUnit.DIVISION_SCALE, RoundingMode.HALF_DOWN));
				}
				weightedPo.setOrdinal(ordinal++);
				addEntity(weightedPo, PoProcess.class, processId);
			}
		}
	}
	
	/**
	 * Updates the process dependency tree (ProcessParents) after add or edit of process.
	 * Only applies for StorageRelocation.
	 * @param processId
	 */
	public void setRelocationProcessParents(Integer processId) {
		removeOldProcessParents(processId);
		List<UsedProcess> usedProcesses = getProcessRepository().findRelocationUsedProcess(processId);
		addUsedProcesses(usedProcesses, processId);
	}
	
	/**
	 * Updates the process dependency tree (ProcessParents)  after add or edit of process.
	 * Only applies for TransactionProcess.
	 * @param processId
	 */
	public void setTransactionProcessParents(Integer processId) {
		removeOldProcessParents(processId);
		List<UsedProcess> usedProcesses = getProcessRepository().findTransactionUsedProcess(processId);
		addUsedProcesses(usedProcesses, processId);
	}
	
	/**
	 * Removes process dependency tree branches (ProcessParents) connected to given process.
	 * @param processId
	 */
	private void removeOldProcessParents(Integer processId) {
		List<ProcessParent> oldProcessParents = getProcessRepository().findProcessParentReferences(processId);
		for(ProcessParent processParent: oldProcessParents) {
			getEntityManager().remove(processParent);
		}
	}
	
	/**
	 * Checks that using processes are synchronized (using process should be after used process).
	 * And adds the process dependency tree branches (ProcessParents) for the given usedProcesses.
	 * @param usedProcesses encapsulates ProcessParents and times of the related processes to check the synchronization.
	 * @param processId
	 */
	private void addUsedProcesses(List<UsedProcess> usedProcesses, Integer processId) {
		if(usedProcesses != null && !usedProcesses.isEmpty()) {
			int ordinal = 0;
			for(UsedProcess usedProcess: usedProcesses) {
				//check processes are in order, for consistency when searching inventory at point of time.
				if(usedProcess.getUsingProcessRecordedTime().isBefore(usedProcess.getRecordedTime())) {
					throw new IllegalArgumentException("Process can't have a date earlier than a used process.\n "
							+ "Either change the current process time, or edit the used process time.");
				}
				ProcessParent processParent = usedProcess.getProcessParent();
				processParent.setOrdinal(ordinal++);
				addEntity(processParent, PoProcess.class, processId);
			}
		}
	}
	
	/**
	 * Called when editing a relocation process.
	 * Use before editing to check if a processed item of the given process is already used by another process.
	 * Only applies for StorageRelocation.
	 * @param relocation
	 * @throws AccessControlException if a removed item is referenced by a later process.
	 */
	public void checkRemovingUsedProduct(RelocationProcessDTO relocation) {
		HashSet<Integer> storageIds = new HashSet<Integer>();
		for(StorageMovesGroupDTO mg: CollectionItemWithGroup.safeCollection(relocation.getStorageMovesGroups())) {
			storageIds.addAll((mg.getStorageMoves().stream().map(StorageMoveDTO::getId).filter(i -> i != null).collect(Collectors.toSet())));
		}
		if(getProcessRepository().isRelocationRemovingUsedProduct(relocation.getId(), storageIds)) {
			throw new AccessControlException("Process items can't be edited because they are already in use");
		}		
	}
	
	/**
	 * Called when editing a process with product.
	 * Use before editing to check if a processed item of the given process is already used by another process.
	 * Only applies for ProcessWithProduct.
	 * @param relocation
	 * @throws AccessControlException if a removed item is referenced by a later process.
	 */
	public <T extends ProcessWithProductDTO<?>> void checkRemovingUsedProduct(T process) {
		HashSet<Integer> storageIds = new HashSet<Integer>();
		for(ProcessItemDTO pi: CollectionItemWithGroup.safeCollection((process.getProcessItems()))) {
			storageIds.addAll(pi.getStorageForms().stream().map(StorageDTO::getId).filter(i -> i != null).collect(Collectors.toSet()));
		}
		if(getProcessRepository().isRemovingUsedProduct(process.getId(), storageIds)) {
			throw new AccessControlException("Process items can't be edited because they are already in use");
		}	
	}
	
	/**
	 * Called when editing a process with product.
	 * Checks the synchronization of processes related to the given process and if produced amount is sufficient for using processes.
	 * Recorded time of a process using a product of a given process needs to be after the time of the referenced process.
	 * Amount used for a process storages can't exceed the storage amount.
	 * @param <T> extends ProcessWithProductDTO
	 * @param process
	 * @throws IllegalArgumentException if one of the using processes is before the time of this process.
	 * @throws IllegalArgumentException if other processes using product exceed the product amount.
	 */
	public <T extends ProcessWithProductDTO<?>> void checkUsingProcesessConsistency(T process) {
		//check that processes who use this product are synchronized (are later) with this edited process
		if(!getProcessRepository().isProcessSynchronized(process.getId())) {
			throw new IllegalArgumentException("Process recorded time is after a process using it's product");			
		}
		
		//checks if not reducing produced amounts already used by other processes
		Stream<StorageBalance> storageBalances = getInventoryRepository().findProducedStorageBalances(process.getId());		
		if(storageBalances.anyMatch(b -> !b.isLegal())) {
			throw new IllegalArgumentException("Process produced amounts can't be reduced because already in use");
		}
	}
	
	/**
	 * Called when editing a relocation process.
	 * Checks the synchronization of processes related to the given process and if produced amount is sufficient for using processes.
	 * Recorded time of a process using a product of a given process needs to be after the time of the referenced process.
	 * Amount used for a process storages can't exceed the storage amount.
	 * @param process
	 * @throws IllegalArgumentException if one of the using processes is before the time of this process.
	 * @throws IllegalArgumentException if other processes using product exceed the product amount.
	 */
	public void checkUsingProcesessConsistency(RelocationProcessDTO relocation) {
		//check that processes who use this product are synchronized (are later) with this edited process
		if(!getProcessRepository().isProcessSynchronized(relocation.getId())) {
			throw new IllegalArgumentException("Process recorded time is after a process using it's product");			
		}
		
		//checks if not reducing produced amounts already used by other processes
		Stream<StorageBalance> storageBalances = getInventoryRepository().findStorageMoveBalances(relocation.getId());
		if(!storageBalances.allMatch(b -> b.isLegal())) {
			throw new IllegalArgumentException("Process moved amounts can't be reduced because already in use");
		}	
	}

	/**
	 * Called when editing a relocation process.
	 * Checks that the process references are a Directed Acyclic graph, so there is no self reference.
	 * Can be used for Transaction or relocation processes, 
	 * but not a risk in production transaction processes since they follow a predetemined order.
	 * @param usedPos item with po of all items used by given process.
	 * @param processId
	 * @throws IllegalArgumentException if given process is using it's descendant.
	 */
	public void checkDAGmaintained(List<ItemAmountWithPoCode> usedPos, @NonNull Integer processId) {
		Set<Integer> descendants = getProcessDescendants(usedPos.stream()
				.map(i -> i.getPoCode().getId()).collect(Collectors.toSet())
				.stream().toArray(Integer[]::new), processId);
//		if(descendants.stream().anyMatch(i -> i.equals(processId))) {
//			throw new IllegalArgumentException("Process using it's descendant");
//		}
		if(descendants.contains(processId)) {
			throw new IllegalArgumentException("Process using it's descendant");
		}
	}
		
	/**
	 * Retrieves and sets the process items for setting in storage moves.
	 * User only sets storages, but storageMoves have to reference the storage processItem too.
	 * @param storageMovesGroups List<StorageMovesGroupDTO>
	 */
	public void setStorageMovesProcessItem(List<StorageMovesGroupDTO> storageMovesGroups) {
		List<StorageMoveDTO> storageMoves = new ArrayList<>();
		for(StorageMovesGroupDTO group: storageMovesGroups) {
			group.getStorageMoves().forEach(storageMoves::add);
		}
		Map<Integer, StorageBase> storageMap = getRelocationRepository().findStoragesById(
				storageMoves.stream()
				.mapToInt(sm -> sm.getStorage().getId())
				.toArray())
				.collect(Collectors.toMap(StorageBase::getId, Function.identity()));
		storageMoves.forEach(move -> {
			StorageBase storageBase = storageMap.get(move.getStorage().getId());
			move.setProcessItem(new DataObject<ProcessItem>(storageBase.getProcessItem()));
			move.setUnitAmount(storageBase.getUnitAmount());
		});
	}
	
	/**
	 * Checks if relocation changes the amount of product.
	 * If it does, then sends appropriate alerts.
	 * @param processId
	 */
	public void checkRelocationBalance(Integer processId) {
		List<ProcessItemTransactionDifference> differences = getRelocationRepository().findRelocationDifferences(processId);		
		for(ProcessItemTransactionDifference d: differences) {
			if(d.getDifference().signum() != 0) {
				sendMessageAlerts(processId, "Relocated process items don't have matching amounts");
			}
		}
	}
}
