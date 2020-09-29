package com.avc.mis.beta.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.processinfo.ItemCountDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.query.ItemTransactionDifference;
import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.entities.processinfo.StorageBase;
import com.avc.mis.beta.entities.processinfo.StorageMove;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.RelocationRepository;
import com.avc.mis.beta.repositories.TransferRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

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

	
	public List<ProcessRow> getStorageTransfersTable() {
		List<ProcessRow> transferRows = getTransferRepository().findProcessByType(ProcessName.STORAGE_TRANSFER);
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getTransferRepository()
				.findAllUsedItemsByProcessType(ProcessName.STORAGE_TRANSFER)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ProductionProcessWithItemAmount>> producedMap = getTransferRepository()
				.findAllProducedItemsByProcessType(ProcessName.STORAGE_TRANSFER)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ProductionProcessWithItemAmount>> countMap = getTransferRepository()
				.findAllItemsCounts(ProcessName.STORAGE_TRANSFER)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		for(ProcessRow row: transferRows) {
			row.setUsedItems(usedMap.get(row.getId()));
			row.setProducedItems(producedMap.get(row.getId()));
			row.setItemCounts(countMap.get(row.getId()));
		}		
		
		return transferRows;
	}
	
	public List<ProcessRow> getStorageRelocationTable() {
		List<ProcessRow> relocationRows = getRelocationRepository().findProcessByType(ProcessName.STORAGE_RELOCATION);
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getRelocationRepository()
				.findAllMovedItemsByProcessType()
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ProductionProcessWithItemAmount>> countMap = getRelocationRepository()
				.findAllItemsCounts(ProcessName.STORAGE_RELOCATION)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		for(ProcessRow row: relocationRows) {
			row.setUsedItems(usedMap.get(row.getId()));
			row.setItemCounts(countMap.get(row.getId()));
		}		
		
		return relocationRows;
	}

	/**
	 * Adding a record about a storage transfer process
	 * @param transfer StorageTransfer entity object
	 */
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
		StorageMove[] storageMoves = relocation.getStorageMoves();
		Map<Integer, StorageBase> storageMap = getRelocationRepository().findStoragesById(
				Arrays.stream(storageMoves)
				.mapToInt(sm -> sm.getStorage().getId())
				.toArray())
				.collect(Collectors.toMap(StorageBase::getId, Function.identity()));
		for(StorageMove move: storageMoves) {
			move.setProcessItem(storageMap.get(move.getStorage().getId()).getProcessItem());
		}
		dao.addGeneralProcessEntity(relocation);
		//check if storage moves match the amounts of the used item
		checkRelocationBalance(relocation);
	}
	
	
	/**
	 * @param relocation
	 */
	private void checkRelocationBalance(StorageRelocation relocation) {
		Stream<StorageBalance> storageBalances = getInventoryRepository().findStorageMoveBalances(relocation.getId());
		if(!storageBalances.allMatch(b -> b.isLegal())) {
			throw new IllegalArgumentException("Process used item amounts relocated exceed actual amount in inventory");
		}
		
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
			AmountWithUnit producedAmount = d.getProducedAmount();
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
		Optional<StorageTransferDTO> transfer = getTransferRepository().findTransferDTOByProcessId(processId);
		StorageTransferDTO transferDTO = transfer.orElseThrow(
				()->new IllegalArgumentException("No storage transfer with given process id"));
		transferDTO.setProcessItems(ProcessItemDTO
				.getProcessItems(getTransferRepository()
						.findProcessItemWithStorage(processId))
				.stream().collect(Collectors.toSet()));
		transferDTO.setUsedItemGroups(
				UsedItemsGroupDTO.getUsedItemsGroups(
						getTransferRepository().findUsedItemsWithGroup(processId))
				.stream().collect(Collectors.toSet()));
		transferDTO.setItemCounts(
				ItemCountDTO.getItemCounts(
						getTransferRepository().findItemCountWithAmount(processId))
				.stream().collect(Collectors.toSet()));
		return transferDTO;
	}
	
	public StorageRelocationDTO getStorageRelocation(int processId) {
		Optional<StorageRelocationDTO> relocation = getRelocationRepository().findRelocationDTOByProcessId(processId);
		StorageRelocationDTO relocationDTO = relocation.orElseThrow(
				()->new IllegalArgumentException("No storage relocation with given process id"));
		relocationDTO.setStorageMoves(getRelocationRepository().findStorageMoveDTOsByProcessId(processId));
		relocationDTO.setItemCounts(
				ItemCountDTO.getItemCounts(
						getTransferRepository().findItemCountWithAmount(processId))
				.stream().collect(Collectors.toSet()));
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
		//check used items amounts don't exceed the storage amounts
		dao.editGeneralProcessEntity(relocation);
		checkRelocationBalance(relocation);
	}
	
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

	public List<ProcessItemInventory> getInventoryByPo(Integer poCodeId) {		
		return getInventory(null, null, null, poCodeId);		
	}
	
	public List<ProcessItemInventory> getInventoryByItem(Integer itemId) {		
		return getInventory(null, null, itemId, null);
	}
	
	public List<ProcessItemInventory> getInventoryByItemCategories(@NonNull ItemCategory[] itemCategories) {		
		return getInventory(null, itemCategories, null, null);
	}
	
	/**
	 * Gets all information of items in the inventory, for provided supply group, item or po code.
	 * If one of the parameters are null than will ignore that constraint.
	 * For each stored item in inventory, provides information on the process item and balances,
	 * with list of storages that contain amounts used and totals.
	 * Items are considered in inventory if process status is final and it's not completely used.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemCategories constrain to only items from given category, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @param poCodeId constrain to only this po, if null than any.
	 * @return List of ProcessItemInventory
	 */
	public List<ProcessItemInventory> getInventory(SupplyGroup supplyGroup, ItemCategory[] itemCategories, Integer itemId, Integer poCodeId) {
		boolean checkCategories = (itemCategories != null);
		List<InventoryProcessItemWithStorage> processItemWithStorages =
				getInventoryRepository().findInventoryProcessItemWithStorage(checkCategories, itemCategories, supplyGroup, itemId, poCodeId);	
		
		return ProcessItemInventory.getProcessItemInventoryRows(processItemWithStorages);
		
	}

	/**
	 * Gets all information of items in the inventory, for provided supply group, item or po code.
	 * If one of the parameters are null than will ignore that constraint.
	 * For each stored item in inventory, provides information on the process item and balances.
	 * Items are considered in inventory if process status is final and it's not completely used.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemCategory constrain to only items from given category, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @param poCodeId constrain to only this po, if null than any.
	 * @return List of ProcessItemInventoryRow
	 */
	public List<ProcessItemInventoryRow> getInventoryRows(SupplyGroup supplyGroup, ItemCategory[] itemCategories, Integer itemId, Integer poCodeId) {
		boolean checkCategories = (itemCategories != null);
		return getInventoryRepository().findInventoryProcessItemRows(checkCategories, itemCategories, supplyGroup, itemId, poCodeId);	
	}
}
