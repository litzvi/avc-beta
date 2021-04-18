package com.avc.mis.beta.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithPoCode;
import com.avc.mis.beta.dto.query.ItemTransactionDifference;
import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.query.UsedProcess;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.InventoryUseRepository;
import com.avc.mis.beta.repositories.RelocationRepository;
import com.avc.mis.beta.repositories.TransferRepository;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Service for recording and receiving Warehouse activity and information
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class WarehouseManagement {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private InventoryRepository inventoryRepository;
	@Autowired private TransferRepository transferRepository;
	@Autowired private RelocationRepository relocationRepository;
	@Autowired private InventoryUseRepository inventoryUseRepository;
	@Autowired private ValueTablesRepository valueTablesRepository;
	@Autowired private ProcessInfoReader processInfoReader;

	
	public List<ProcessRow> getStorageTransfers() {
		return getStorageTransfersByPoCode(null);
	}
	
	public List<ProcessRow> getStorageTransfersByPoCode(Integer poCodeId) {
		return dao.getProcessesByTypeAndPoCode(StorageTransfer.class, ProcessName.STORAGE_TRANSFER, poCodeId, null, true);
	}

	public List<ProcessRow> getStorageRelocations() {
		return getStorageRelocationsByPoCode(null, null);
	}
	
	public List<ProcessRow> getStorageRelocations(ProductionFunctionality productionFunctionality) {
		return getStorageRelocationsByPoCode(null, productionFunctionality);
	}
	
	public List<ProcessRow> getStorageRelocationsByPoCode(Integer poCodeId, ProductionFunctionality productionFunctionality) {
		return dao.getProcessesByTypeAndPoCode(StorageRelocation.class, ProcessName.STORAGE_RELOCATION, poCodeId, productionFunctionality, true);
//		List<ProcessRow> relocationRows = getRelocationRepository().findProcessByType(ProcessName.STORAGE_RELOCATION, poCodeId, productionFunctionality, true);
//		int[] processIds = relocationRows.stream().mapToInt(ProcessRow::getId).toArray();
//		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getRelocationRepository()
//				.findAllMovedItemsByProcessIds(processIds)
//				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
//		Map<Integer, List<ProductionProcessWithItemAmount>> countMap = getRelocationRepository()
//				.findAllItemsCountsByProcessIds(processIds)
//				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
//		for(ProcessRow row: relocationRows) {
//			row.setUsedItems(usedMap.get(row.getId()));
//			row.setItemCounts(countMap.get(row.getId()));
//		}		
//		return relocationRows;
	}
	
	public List<ProcessRow> getInventoryUses() {
		return getInventoryUses(null);
	}
	
	public List<ProcessRow> getInventoryUses(Integer poCodeId) {
		return dao.getProcessesByTypeAndPoCode(InventoryUse.class, ProcessName.GENERAL_USE, poCodeId, null, true);
//		List<ProcessRow> inventoryUseRows = getInventoryUseRepository().findProcessByType(ProcessName.GENERAL_USE, poCodeId, null, true);
//		int[] processIds = inventoryUseRows.stream().mapToInt(ProcessRow::getId).toArray();
//		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getInventoryUseRepository()
//				.findAllUsedItemsByProcessIds(processIds)
//				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
//		for(ProcessRow row: inventoryUseRows) {
//			row.setUsedItems(usedMap.get(row.getId()));
//		}		
//		return inventoryUseRows;
	}

	/**
	 * Adding a record about a storage transfer process
	 * @param transfer StorageTransfer entity object
	 */
	@Deprecated
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addStorageTransfer(StorageTransfer transfer) {
		transfer.setProcessType(dao.getProcessTypeByValue(ProcessName.STORAGE_TRANSFER));
		dao.addTransactionProcessEntity(transfer);
		//check if process items match the used item (items are equal, perhaps also check amounts difference and send warning)
		checkTransferBalance(transfer);
	}
	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addStorageRelocation(StorageRelocation relocation) {
		relocation.setProcessType(dao.getProcessTypeByValue(ProcessName.STORAGE_RELOCATION));
		setStorageMovesProcessItem(relocation.getStorageMovesGroups());
		dao.addGeneralProcessEntity(relocation);
		dao.setPoWeights(relocation);
		dao.setUsedProcesses(relocation);
		//check if storage moves match the amounts of the used item
		checkRelocationBalance(relocation);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralInventoryUse(InventoryUse inventoryUse) {
		//Check that used items are from general
		if(!isUsedInItemGroup(inventoryUse.getUsedItemGroups(), ItemGroup.GENERAL)) {
			throw new IllegalArgumentException("Inventory use can only be for GENERAL item groups");
		}		
				
		inventoryUse.setProcessType(dao.getProcessTypeByValue(ProcessName.GENERAL_USE));
		dao.addGeneralProcessEntity(inventoryUse);
//		set weightedPos weight for general (no weight)
		dao.setUsedProcesses(inventoryUse);
	}	
	
	private boolean isUsedInItemGroup(UsedItemsGroup[] usedItemGroups, ItemGroup itemGroup) {
		Set<Integer> usedStorageIds = null;
		for(UsedItemsGroup uig: usedItemGroups) {
			usedStorageIds = Arrays.stream(uig.getUsedItems()).map(UsedItem::getStorage).map(StorageBase::getId).collect(Collectors.toSet());
		}
		if(usedStorageIds != null) {
			List<ItemWithUnitDTO> items = getValueTablesRepository().findStoragesItems(usedStorageIds);
			if(items.stream().anyMatch(i -> i.getGroup() != itemGroup)) {
				return false;
			}
		}
		return true;
	}

	public InventoryUseDTO getInventoryUse(int processId) {
		InventoryUseDTO inventoryUseDTO = new InventoryUseDTO();
		inventoryUseDTO.setGeneralProcessInfo(getInventoryUseRepository()
				.findGeneralProcessInfoByProcessId(processId, InventoryUse.class)
				.orElseThrow(
						()->new IllegalArgumentException("No inventory use with given process id")));
		inventoryUseDTO.setPoProcessInfo(getInventoryUseRepository()
				.findPoProcessInfoByProcessId(processId, InventoryUse.class).orElse(null));
		
		getProcessInfoReader().setTransactionProcessCollections(inventoryUseDTO);
		
		return inventoryUseDTO;
	}
		
	/**
	 * @param relocation
	 */
	private void checkRelocationOutputSufficent(StorageRelocation relocation) {
		Stream<StorageBalance> storageBalances = getInventoryRepository().findStorageMoveBalances(relocation.getId());
		if(!storageBalances.allMatch(b -> b.isLegal())) {
			throw new IllegalArgumentException("Process moved amounts can't be reduced because already in use");
		}	
	}
	
	private void checkRelocationBalance(StorageRelocation relocation) {
		List<ProcessItemTransactionDifference> differences = getRelocationRepository().findRelocationDifferences(relocation.getId());		
		for(ProcessItemTransactionDifference d: differences) {
			if(d.getDifference().signum() != 0) {
				dao.sendMessageAlerts(relocation, "Relocated process items don't have matching amounts");
			}
		}
	}
	
	private void checkTransferBalance(StorageTransfer transfer) {
		List<ItemTransactionDifference> differences = getTransferRepository().findTransferDifferences(transfer.getId());
		
		for(ItemTransactionDifference d: differences) {
			BigDecimal producedAmount = d.getProducedAmount();
			if (producedAmount == null /* || producedAmount.compareTo(BigDecimal.ZERO) == 0 */) {
				throw new IllegalArgumentException("Storage transfer can't change item");
			}
			if(d.getDifference().signum() < 0) {
				dao.sendMessageAlerts(transfer, "Transffered items don't have matching amounts");
			}
		}
	}

	/**
	 * Get a full storage transfer process information
	 * @param processId id of the StorageTransfer process
	 * @return StorageTransferDTO
	 */
	public StorageTransferDTO getStorageTransfer(int processId) {
		StorageTransferDTO transferDTO = new StorageTransferDTO();
		transferDTO.setGeneralProcessInfo(getTransferRepository()
				.findGeneralProcessInfoByProcessId(processId, StorageTransfer.class)
				.orElseThrow(
						()->new IllegalArgumentException("No storage transfer with given process id")));
		transferDTO.setPoProcessInfo(getTransferRepository()
				.findPoProcessInfoByProcessId(processId, StorageTransfer.class)
				.orElseThrow(
						()->new IllegalArgumentException("No storage transfer with given process id")));
		
//		Optional<StorageTransferDTO> transfer = getTransferRepository().findTransferDTOByProcessId(processId);
//		StorageTransferDTO transferDTO = transfer.orElseThrow(
//				()->new IllegalArgumentException("No storage transfer with given process id"));
		transferDTO.setProcessItems(
				CollectionItemWithGroup.getFilledGroups(
						getTransferRepository()
						.findProcessItemWithStorage(processId)));
//				ProcessItemDTO.getProcessItems(getTransferRepository()
//						.findProcessItemWithStorage(processId)));
		transferDTO.setUsedItemGroups(
				CollectionItemWithGroup.getFilledGroups(
						getTransferRepository()
						.findUsedItemsWithGroup(processId)));
//				UsedItemsGroupDTO.getUsedItemsGroups(
//						getTransferRepository()
//						.findUsedItemsWithGroup(processId)));
		transferDTO.setItemCounts(
				CollectionItemWithGroup.getFilledGroups(
						getTransferRepository()
						.findItemCountWithAmount(processId)));
//				ItemCountDTO.getItemCounts(
//						getTransferRepository()
//						.findItemCountWithAmount(processId)));
		return transferDTO;
	}
	
	public StorageRelocationDTO getStorageRelocation(int processId) {
		StorageRelocationDTO relocationDTO = new StorageRelocationDTO();
		relocationDTO.setGeneralProcessInfo(getRelocationRepository()
				.findGeneralProcessInfoByProcessId(processId, StorageRelocation.class)
				.orElseThrow(
						()->new IllegalArgumentException("No storage relocation with given process id")));
		relocationDTO.setPoProcessInfo(getRelocationRepository()
				.findPoProcessInfoByProcessId(processId, StorageRelocation.class)
				.orElseThrow(
						()->new IllegalArgumentException("No po code for given process id")));

//		Optional<StorageRelocationDTO> relocation = getRelocationRepository().findRelocationDTOByProcessId(processId);
//		StorageRelocationDTO relocationDTO = relocation.orElseThrow(
//				()->new IllegalArgumentException("No storage relocation with given process id"));
		
		relocationDTO.setStorageMovesGroups(
				CollectionItemWithGroup.getFilledGroups(
						getRelocationRepository()
						.findStorageMovesWithGroup(processId)));
//				StorageMovesGroupDTO.getStorageMoveGroups(
//						getRelocationRepository()
//						.findStorageMovesWithGroup(processId)));
//		relocationDTO.setStorageMoves(getRelocationRepository().findStorageMoveDTOsByProcessId(processId));
		relocationDTO.setItemCounts(
				CollectionItemWithGroup.getFilledGroups(
						getTransferRepository()
						.findItemCountWithAmount(processId)));
//		relocationDTO.setItemCounts(
//				ItemCountDTO.getItemCounts(
//						getTransferRepository()
//						.findItemCountWithAmount(processId)));
		return relocationDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editStorageTransfer(StorageTransfer transfer) {
		//check used items amounts don't exceed the storage amounts
		dao.editTransactionProcessEntity(transfer);
		checkTransferBalance(transfer);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editStorageRelocation(StorageRelocation relocation) {
		dao.checkRelocationRemovingUsedProduct(relocation);
		setStorageMovesProcessItem(relocation.getStorageMovesGroups());
		dao.editGeneralProcessEntity(relocation);
		dao.setUsedProcesses(relocation);;
		List<ItemAmountWithPoCode> usedPos = dao.setPoWeights(relocation);
		getProcessInfoReader().checkDAGmaintained(usedPos, relocation.getId());
		checkRelocationOutputSufficent(relocation);
		checkRelocationBalance(relocation);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editGeneralInventoryUse(InventoryUse inventoryUse) {
		//Check that used items are from general
		if(!isUsedInItemGroup(inventoryUse.getUsedItemGroups(), ItemGroup.GENERAL)) {
			throw new IllegalArgumentException("Inventory use can only be for GENERAL item groups");
		}			
		dao.editGeneralProcessEntity(inventoryUse);
//		set weightedPos weight for general
		dao.setUsedProcesses(inventoryUse);
	}
	
	private void setStorageMovesProcessItem(StorageMovesGroup[] storageMovesGroups) {
		List<StorageMove> storageMoves = new ArrayList<StorageMove>();
		for(StorageMovesGroup group: storageMovesGroups) {
			Arrays.stream(group.getStorageMoves()).forEach(storageMoves::add);
		}
		Map<Integer, StorageBase> storageMap = getRelocationRepository().findStoragesById(
				storageMoves.stream()
				.mapToInt(sm -> sm.getStorage().getId())
				.toArray())
				.collect(Collectors.toMap(StorageBase::getId, Function.identity()));
		storageMoves.forEach(move -> {
			move.setProcessItem(storageMap.get(move.getStorage().getId()).getProcessItem());
		});
	}
	
	public List<ProcessItemInventory> getAvailableInventory(
			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
			Integer itemId, Integer[] poCodeIds, Integer excludeProcessId) {
		
		boolean checkProductionUses = (productionUses != null);
		boolean checkFunctionalities = (functionalities != null);
		boolean checkPoCodes = (poCodeIds != null);
		Integer[] excludedProcessIds = null;
		if(poCodeIds != null && excludeProcessId != null) {
			excludedProcessIds = getProcessInfoReader().getProcessDescendants(poCodeIds, excludeProcessId);
		}
		boolean checkExcludedProcessIds = (excludedProcessIds != null && excludedProcessIds.length > 0);
		List<StorageInventoryRow> storageInventoryRows = getInventoryRepository()
				.findAvailableInventoryByStorage(checkProductionUses, productionUses, 
						checkFunctionalities, functionalities, 
						group, itemId, 
						checkPoCodes, poCodeIds,
						checkExcludedProcessIds, excludedProcessIds);
				
		return CollectionItemWithGroup.getFilledGroups(storageInventoryRows, getInventoryRepository()::findProcessItemInventory);
	}	
	
//	public List<ProcessItemInventory> getAvailableInventory(ItemGroup group, ProductionUse[] productionUses, Integer itemId, int poCodeId) {
//		int[] poCodeIds = new int[] {poCodeId};
//		return getAvailableInventory(group, productionUses, itemId, poCodeIds);
//	}
	
//	//need to make sure currently in inventory - used for test
//	@Deprecated
//	public List<PoProcessItemEntry> getProcessItemsWithPoByPo(Integer poCodeId) {		
//		return ProcessItemDTO.getProcessItemsWithPo(getInventoryRepository().findProcessItemWithStorageByPoCode(poCodeId));
//	}
//	
//	//need to make sure currently in inventory - used for test
//	@Deprecated
//	public List<PoProcessItemEntry> getProcessItemsWithPoByItem(Integer itemId) {		
//		return ProcessItemDTO.getProcessItemsWithPo(getInventoryRepository().findProcessItemWithStorageByItem(itemId));
//	}

//	public List<ProcessItemInventory> getCashewAvailableInventoryByPo(Integer poCodeId) {		
//		return getAvailableInventory(ItemGroup.PRODUCT, null, null, poCodeId);		
//	}
	
//	/**
//	 * Gets inventory for all item groups (Product, General, Waste etc.)
//	 * @param poCodeId
//	 * @return
//	 */
//	public List<ProcessItemInventory> getAllAvailableInventoryByPo(Integer poCodeId) {		
//		return getAvailableInventory(null, null, null, poCodeId);		
//	}
	
//	public List<ProcessItemInventory> getAvailableInventoryByItem(Integer itemId) {		
//		return getAvailableInventory(null, null, itemId, null);
//	}
	
//	public List<ProcessItemInventory> getAvailableInventoryByItemProductionUses(@NonNull ProductionUse[] productionUses) {		
//		return getAvailableInventory(null, productionUses, null, null);
//	}
	
//	/**
//	 * Gets all information of items in available inventory, for provided supply group, item or po code.
//	 * If one of the parameters are null than will ignore that constraint.
//	 * For each stored item in inventory, provides information on the process item and balances,
//	 * with list of storages that contain amounts used and totals.
//	 * Available inventory for querying what items are available for use by a process.
//	 * Items are considered available inventory if the producing process status is final 
//	 * and it's not completely used by another using process where the using process isn't cancelled.
//	 * @param supplyGroup constrain to only this supply group, if null than any.
//	 * @param itemCategories constrain to only items from given category, if null than any.
//	 * @param itemId constrain to only this item, if null than any.
//	 * @param poCodeId constrain to only this po, if null than any.
//	 * @return List of ProcessItemInventory
//	 */
//	@Deprecated //next method - new one
//	public List<ProcessItemInventory> getAvailableInventory_old(ItemGroup group, ProductionUse[] productionUses, Integer itemId, Integer poCodeId) {
//		boolean checkProductionUses = (productionUses != null);
//		List<InventoryProcessItemWithStorage> processItemWithStorages =
//					getInventoryRepository().findAvailableInventoryProcessItemWithStorage(checkProductionUses, productionUses, group, itemId, poCodeId);
//
//		return CollectionItemWithGroup.getFilledGroups(processItemWithStorages);
//	}


}
