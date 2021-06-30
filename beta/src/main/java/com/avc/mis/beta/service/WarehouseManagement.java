package com.avc.mis.beta.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.query.ItemTransactionDifference;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.TransferRepository;
import com.avc.mis.beta.service.report.InventoryUseReports;
import com.avc.mis.beta.service.report.ProductionProcessReports;
import com.avc.mis.beta.service.report.StorageRelocationReports;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

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

	public List<ProcessItemInventory> getAvailableInventory(
			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
			Integer itemId, Integer[] poCodeIds, Integer excludeProcessId) {
		
		boolean checkProductionUses = (productionUses != null);
		boolean checkFunctionalities = (functionalities != null);
		boolean checkPoCodes = (poCodeIds != null);
		Integer[] excludedProcessIds = null;
		if(poCodeIds != null && excludeProcessId != null) {
			Set<Integer> excludedProcessIdsSet = dao.getProcessDescendants(poCodeIds, excludeProcessId);
			excludedProcessIdsSet.add(excludeProcessId);
			excludedProcessIds = excludedProcessIdsSet.toArray(new Integer[excludedProcessIdsSet.size()]);
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
	
//	--------------------------------------Items available in inventory----------------------------------------
	
	/**
	 * Gets the item's BasicValueEntity for all Cashew items available in inventory - id and value.
	 * Cashew in inventory - process outcomes that are finalized. 
	 * Can be used for choosing a item for use.
	 * @return Set of BasicValueEntity for all inventory Cashew.
	 */
	public Set<BasicValueEntity<Item>> findCashewAvailableInventoryItems() {
		return findAvailableInventoryItems(false,  null, false, null, ItemGroup.PRODUCT, null, null);		
	}
	
	/**
	 * Gets the item's BasicValueEntity for all General items available in inventory - id and value.
	 * General inventory - process outcomes that are finalized. 
	 * Can be used for choosing a item for use.
	 * @return Set of BasicValueEntity for all General inventory.
	 */
	public Set<BasicValueEntity<Item>> findGeneralAvailableInventoryItems() {
		return findAvailableInventoryItems(false,  null, false, null, ItemGroup.GENERAL, null, null);		
	}
	
	
	public Set<BasicValueEntity<Item>> findAvailableInventoryItems(@NonNull ProductionUse[] productionUses) {
		return findAvailableInventoryItems(true, productionUses, false, null, null, null, null);		
	}
	
	public Set<BasicValueEntity<Item>> findAvailableInventoryItems(@NonNull ProductionUse[] productionUses, ProductionFunctionality[] functionalities) {
		boolean checkFunctionalities = (functionalities != null);
		return findAvailableInventoryItems(true, productionUses, checkFunctionalities, functionalities, null, null, null);		
	}
	
	/**
	 * Gets the item's BasicValueEntity for all items for the given item group available in inventory - id and value.
	 * Can be used for choosing a an item from a given group. e.g. waste.
	 * @param group
	 * @return Set of BasicValueEntity
	 */
	public Set<BasicValueEntity<Item>> findAvailableInventoryItems(ItemGroup group) {
		return findAvailableInventoryItems(false,  null, false, null, group, null, null);		
	}
	
	public Set<BasicValueEntity<Item>> findAvailableInventoryItems(ProductionUse[] productionUses, ItemGroup group) {
		return findAvailableInventoryItems(true,  productionUses, false, null, group, null, null);		
	}
	
	private Set<BasicValueEntity<Item>> findAvailableInventoryItems(
			boolean checkProductionUses, ProductionUse[] productionUses, 
			boolean checkFunctionalities, ProductionFunctionality[] functionalities,
			ItemGroup itemGroup, Integer itemId,
			Integer poCodeId) {
		return getInventoryRepository().findAvailableInventoryItemsByType(checkProductionUses, productionUses, 
				checkFunctionalities, functionalities, itemGroup, itemId, poCodeId);		
	}
	
//	--------------------------------------PO codes available in inventory----------------------------------------
	
	/**
	 * Gets the po code basic information of all Cashew in inventory - id, poCode and supplier.
	 * Cashew in inventory - process outcomes that are finalized. 
	 * Can be used for choosing a po for factory processing.
	 * @return Set of PoCodeBasic for all inventory Cashew.
	 */
	public Set<PoCodeBasic> findCashewAvailableInventoryPoCodes() {
		return findAvailableInventoryPoCodes(false,  null, false, null, ItemGroup.PRODUCT, null);		
	}
	
	/**
	 * Gets the po code basic information of all General items in inventory - id, poCode and supplier.
	 * General inventory - process outcomes that are finalized. 
	 * Can be used for choosing a po for factory processing.
	 * @return Set of PoCodeBasic for all General inventory.
	 */
	public Set<PoCodeBasic> findGeneralAvailableInventoryPoCodes() {
		return findAvailableInventoryPoCodes(false,  null, false, null, ItemGroup.GENERAL, null);		
	}
	
	/**
	 * Gets the basic information of all po codes for the given item in inventory - id, poCode and supplier.
	 * Can be used for choosing a po for factory processing of a certain item.
	 * @param itemId id of the item
	 * @return Set of PoCodeBasic
	 */
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(Integer itemId) {
		return findAvailableInventoryPoCodes(false, null, false, null, null, itemId);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses) {
		return findAvailableInventoryPoCodes(true, productionUses, false, null, null, null);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses, ProductionFunctionality[] functionalities) {
		boolean checkFunctionalities = (functionalities != null);
		return findAvailableInventoryPoCodes(true, productionUses, checkFunctionalities, functionalities, null, null);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ItemGroup group) {
		return findAvailableInventoryPoCodes(false,  null, false, null, group, null);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ProductionUse[] productionUses, ItemGroup group) {
		return findAvailableInventoryPoCodes(true,  productionUses, false, null, group, null);		
	}
	
	private Set<PoCodeBasic> findAvailableInventoryPoCodes(
			boolean checkProductionUses, ProductionUse[] productionUses, 
			boolean checkFunctionalities, ProductionFunctionality[] functionalities,
			ItemGroup itemGroup, Integer itemId) {
		return getInventoryRepository().findAvailableInventoryPoCodeByType(checkProductionUses, productionUses, 
				checkFunctionalities, functionalities, itemGroup, itemId);		
	}

	
	//----------------------------Duplicate in InventoryUses - Should remove------------------------------------------
	
	@Autowired private InventoryUses inventoryUseService;
	@Autowired private InventoryUseReports inventoryUseReports;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralInventoryUse(InventoryUse inventoryUse) {
		getInventoryUseService().addGeneralInventoryUse(inventoryUse);
	}	
	

	public InventoryUseDTO getInventoryUse(int processId) {
		return getInventoryUseService().getInventoryUse(processId);
	}
		
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editGeneralInventoryUse(InventoryUse inventoryUse) {
		getInventoryUseService().editGeneralInventoryUse(inventoryUse);
	}
	
	public List<ProcessRow> getInventoryUses() {
		return getInventoryUseReports().getInventoryUses(ProcessName.GENERAL_USE);
	}
	
	public List<ProcessRow> getInventoryUses(Integer poCodeId) {
		return getInventoryUseReports().getInventoryUses(ProcessName.GENERAL_USE, poCodeId);
	}
	
	//----------------------------Duplicate in StorageRelocations - Should remove------------------------------------------

	@Autowired private StorageRelocations relocationService;
	@Autowired private StorageRelocationReports relocationReports;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addStorageRelocation(StorageRelocation relocation) {
		getRelocationService().addStorageRelocation(relocation);
	}

	public StorageRelocationDTO getStorageRelocation(int processId) {
		return getRelocationService().getStorageRelocation(processId);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editStorageRelocation(StorageRelocation relocation) {
		getRelocationService().editStorageRelocation(relocation);
	}
	
	public List<ProcessRow> getStorageRelocations() {
		return getRelocationReports().getStorageRelocations();
	}
	
	public List<ProcessRow> getStorageRelocations(ProductionFunctionality productionFunctionality) {
		return getRelocationReports().getStorageRelocations(productionFunctionality);
	}
	
	public List<ProcessRow> getStorageRelocationsByPoCode(Integer poCodeId, ProductionFunctionality productionFunctionality) {
		return getRelocationReports().getStorageRelocationsByPoCode(poCodeId, productionFunctionality);
	}
	
	//----------------------------StorageTransfers - Deprecated Should remove------------------------------------------

	@Autowired private TransferRepository transferRepository;
	@Autowired private ProductionProcessReports processReportsReader;

	/**
	 * Adding a record about a storage transfer process
	 * @param transfer StorageTransfer entity object
	 */
	@Deprecated
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addStorageTransfer(StorageTransfer transfer) {
		transfer.setProcessType(dao.getProcessTypeByValue(ProcessName.STORAGE_TRANSFER));
		dao.addTransactionProcessEntity(transfer);
		dao.checkUsedInventoryAvailability(transfer);
		dao.setPoWeights(transfer);
		dao.setUsedProcesses(transfer);
		//check if process items match the used item (items are equal, perhaps also check amounts difference and send warning)
		checkTransferBalance(transfer);
	}
	
	public List<ProcessRow> getStorageTransfers() {
		return getStorageTransfersByPoCode(null);
	}
	
	public List<ProcessRow> getStorageTransfersByPoCode(Integer poCodeId) {
		return getProcessReportsReader().getProcessesByTypeAndPoCode(StorageTransfer.class, ProcessName.STORAGE_TRANSFER, poCodeId, null, true, null, null);
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
	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editStorageTransfer(StorageTransfer transfer) {
		//check used items amounts don't exceed the storage amounts
		dao.checkRemovingUsedProduct(transfer);
		
		dao.editTransactionProcessEntity(transfer);
		
		dao.checkUsingProcesessConsistency(transfer);
		dao.checkUsedInventoryAvailability(transfer);
		dao.setPoWeights(transfer);
		dao.setUsedProcesses(transfer);
		checkTransferBalance(transfer);
	}

}
