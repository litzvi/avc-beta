package com.avc.mis.beta.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.process.InventoryUseDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.query.ItemTransactionDifference;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.dto.view.InventoryTransactionAddRow;
import com.avc.mis.beta.dto.view.InventoryTransactionRow;
import com.avc.mis.beta.dto.view.InventoryTransactionSubtractRow;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.PackageType;
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
	
	public static ProductionFunctionality[] EXCLUDED_FUNCTIONALITIES = new ProductionFunctionality[] {
			ProductionFunctionality.LOADING, ProductionFunctionality.GENERAL_USE, ProductionFunctionality.PRODUCT_USE, 
			ProductionFunctionality.QUALITY_CONTROL_CHECK};
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private InventoryRepository inventoryRepository;
	
	public List<ProcessItemInventory> getAvailableInventory(
			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
			Integer itemId, Integer[] poCodeIds, Integer excludeProcessId) {
		Integer[] itemIds = itemId != null ? new Integer[] {itemId} : null;
		return getAvailableInventory(group, productionUses, functionalities, itemIds, null, poCodeIds, excludeProcessId);
	}
	
//	public List<ProcessItemInventory> getAvailableInventory(
//			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
//			Integer itemId, PackageType packageType, Integer[] poCodeIds, Integer excludeProcessId) {
//		Integer[] itemIds = itemId != null ? new Integer[] {itemId} : null;
//		return getAvailableInventory(group, productionUses, functionalities, itemIds, packageType, poCodeIds, excludeProcessId);
//	}

	public List<ProcessItemInventory> getAvailableInventory(
			ItemGroup group, ProductionUse[] productionUses, ProductionFunctionality[] functionalities, 
			Integer[] itemIds, PackageType packageType, Integer[] poCodeIds, Integer excludeProcessId) {
		
		boolean checkProductionUses = (productionUses != null);
		boolean checkFunctionalities = (functionalities != null);
		boolean checkItemIds = (itemIds != null);
		Integer packageTypeOrdinal = packageType != null ? packageType.ordinal() : null;
		boolean checkPoCodes = (poCodeIds != null);
		Integer[] excludedProcessIds = null;
		if(poCodeIds != null && excludeProcessId != null) {
			Set<Integer> excludedProcessIdsSet = dao.getProcessDescendants(poCodeIds, excludeProcessId);
			excludedProcessIdsSet.add(excludeProcessId);
			excludedProcessIds = excludedProcessIdsSet.toArray(new Integer[excludedProcessIdsSet.size()]);
		}
		boolean checkExcludedProcessIds = (excludedProcessIds != null && excludedProcessIds.length > 0);
		List<StorageInventoryRow> storageInventoryRows = getInventoryRepository()
				.findAvailableInventoryByStorage(EXCLUDED_FUNCTIONALITIES, checkProductionUses, productionUses, 
						checkFunctionalities, functionalities, 
						group, 
						checkItemIds, itemIds, 
						packageTypeOrdinal,
						checkPoCodes, poCodeIds,
						checkExcludedProcessIds, excludedProcessIds);
				
		return CollectionItemWithGroup.getFilledGroups(storageInventoryRows, getInventoryRepository()::findProcessItemInventory);
	}
	
	public List<InventoryTransactionRow> getInventoryTransactions(ItemGroup itemGroup, Integer[] itemIds, Integer[] poCodeIds, 
			LocalDateTime startTime, LocalDateTime endTime) {		
		boolean checkItemIds = (itemIds != null);
		boolean checkPoCodes = (poCodeIds != null);
		
		List<InventoryTransactionAddRow> added = getInventoryRepository().findInventoryTransactionAdditions(
				WarehouseManagement.EXCLUDED_FUNCTIONALITIES, 
//				new ProductionFunctionality[] {ProductionFunctionality.LOADING},
				itemGroup, checkItemIds, itemIds, checkPoCodes, poCodeIds, startTime, endTime);
		
		List<InventoryTransactionSubtractRow> subtracted = getInventoryRepository().findInventoryTransactionSubtractions(
				WarehouseManagement.EXCLUDED_FUNCTIONALITIES, 
				itemGroup, checkItemIds, itemIds, checkPoCodes, poCodeIds, startTime, endTime);
		
		List<InventoryTransactionRow> transactionRows = new ArrayList<InventoryTransactionRow>(added.size() + subtracted.size());
		
		Iterator<InventoryTransactionAddRow> iteratorAdded = added.iterator();
		Iterator<InventoryTransactionSubtractRow> iteratorSubtracted = subtracted.iterator(); 
		InventoryTransactionAddRow addRow = null;
		InventoryTransactionSubtractRow subtractRow = null;
		do {
			if(addRow == null && iteratorAdded.hasNext()) {
				addRow = iteratorAdded.next();
			}
			if(subtractRow == null && iteratorSubtracted.hasNext()) {
				subtractRow = iteratorSubtracted.next();
			}
			
			if(addRow != null && subtractRow != null) {
				if(addRow.compareTo(subtractRow) > 0) {
					transactionRows.add(addRow);
					addRow = null;
				}
				else {
					transactionRows.add(subtractRow);
					subtractRow = null;
				}
			}
			else if(addRow == null) {
				transactionRows.add(subtractRow);
				subtractRow = null;
				iteratorSubtracted.forEachRemaining(i -> transactionRows.add(i));
			}
			else if(subtractRow == null) {
				transactionRows.add(addRow);
				addRow = null;
				iteratorAdded.forEachRemaining(i -> transactionRows.add(i));
			}
			
		} while(addRow != null || subtractRow != null);
//		transactionRows.addAll(added);
//		transactionRows.addAll(subtracted);
		return transactionRows;
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
		return getInventoryRepository().findAvailableInventoryItemsByType(
				EXCLUDED_FUNCTIONALITIES, checkProductionUses, productionUses, 
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
		return findAvailableInventoryPoCodes(false,  null, false, null, ItemGroup.PRODUCT, null, null);		
	}
	
	/**
	 * Gets the po code basic information of all General items in inventory - id, poCode and supplier.
	 * General inventory - process outcomes that are finalized. 
	 * Can be used for choosing a po for factory processing.
	 * @return Set of PoCodeBasic for all General inventory.
	 */
	public Set<PoCodeBasic> findGeneralAvailableInventoryPoCodes() {
		return findAvailableInventoryPoCodes(false,  null, false, null, ItemGroup.GENERAL, null, null);		
	}
	
	/**
	 * Gets the basic information of all po codes for the given item in inventory - id, poCode and supplier.
	 * Can be used for choosing a po for factory processing of a certain item.
	 * @param itemId id of the item
	 * @return Set of PoCodeBasic
	 */
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(Integer itemId) {
		return findAvailableInventoryPoCodes(false, null, false, null, null, itemId, null);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(@NonNull ProductionUse[] productionUses, PackageType packageType) {
		return findAvailableInventoryPoCodes(true, productionUses, false, null, null, null, packageType);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(
			@NonNull ProductionUse[] productionUses, ProductionFunctionality[] functionalities, PackageType packageType) {
		boolean checkFunctionalities = (functionalities != null);
		return findAvailableInventoryPoCodes(true, productionUses, checkFunctionalities, functionalities, null, null, packageType);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ItemGroup group, PackageType packageType) {
		return findAvailableInventoryPoCodes(false,  null, false, null, group, null, packageType);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ProductionUse[] productionUses, ItemGroup group, PackageType packageType) {
		return findAvailableInventoryPoCodes(true,  productionUses, false, null, group, null, packageType);		
	}
	
	public Set<PoCodeBasic> findAvailableInventoryPoCodes(ProductionFunctionality[] functionalities, ItemGroup group, Integer itemId, 
			LocalDateTime startTime, LocalDateTime endTime) {
		boolean checkFunctionalities = (functionalities != null);
		return findAvailableInventoryPoCodes(false, null, checkFunctionalities, functionalities, group, itemId, null, startTime, endTime);		
	}
	
	private Set<PoCodeBasic> findAvailableInventoryPoCodes(
			boolean checkProductionUses, ProductionUse[] productionUses, 
			boolean checkFunctionalities, ProductionFunctionality[] functionalities,
			ItemGroup itemGroup, Integer itemId, PackageType packageType) {
		return findAvailableInventoryPoCodes(checkProductionUses, productionUses, 
				checkFunctionalities, functionalities, itemGroup, itemId, packageType, null, null);
	}
	
	private Set<PoCodeBasic> findAvailableInventoryPoCodes(
			boolean checkProductionUses, ProductionUse[] productionUses, 
			boolean checkFunctionalities, ProductionFunctionality[] functionalities,
			ItemGroup itemGroup, Integer itemId, PackageType packageType, 
			LocalDateTime startTime, LocalDateTime endTime) {
		Integer packageTypeOrdinal = packageType != null ? packageType.ordinal() : null;
		return getInventoryRepository().findAvailableInventoryPoCodeByType(
				EXCLUDED_FUNCTIONALITIES, checkProductionUses, productionUses, 
				checkFunctionalities, functionalities, itemGroup, itemId, packageTypeOrdinal, 
				startTime, endTime);		
	}

	
	//----------------------------Duplicate in InventoryUses - Should remove------------------------------------------
	
	@Autowired private InventoryUses inventoryUseService;
	@Autowired private InventoryUseReports inventoryUseReports;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void addGeneralInventoryUse(InventoryUse inventoryUse) {
		getInventoryUseService().addGeneralInventoryUse(inventoryUse);
	}	
	

	public InventoryUseDTO getInventoryUse(int processId) {
		return getInventoryUseService().getInventoryUse(processId);
	}
		
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
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
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void addStorageRelocation(StorageRelocation relocation) {
		getRelocationService().addStorageRelocation(relocation);
	}

	public StorageRelocationDTO getStorageRelocation(int processId) {
		return getRelocationService().getStorageRelocation(processId);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
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
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
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
	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
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
