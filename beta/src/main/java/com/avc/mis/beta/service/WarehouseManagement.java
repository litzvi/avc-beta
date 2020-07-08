package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.queryRows.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.queryRows.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.TransferRepository;

import lombok.AccessLevel;
import lombok.Getter;

@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class WarehouseManagement {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private InventoryRepository inventoryRepository;
	@Autowired private TransferRepository transferRepository;


	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addStorageTransfer(StorageTransfer transfer) {
		transfer.setProcessType(dao.getProcessTypeByValue(ProcessName.STORAGE_TRANSFER));
		//check used items amounts don't exceed the storage amounts
		dao.addProcessEntity(transfer);
	}
	
	public StorageTransferDTO getStorageTransfer(int processId) {
		Optional<StorageTransferDTO> transfer = getTransferRepository().findTransferDTOByProcessId(processId);
		StorageTransferDTO transferDTO = transfer.orElseThrow(
				()->new IllegalArgumentException("No storage transfer with given process id"));
		transferDTO.setProcessItems(ProcessItemDTO
				.getProcessItems(getTransferRepository()
						.findProcessItemWithStorage(processId))
				.stream().collect(Collectors.toSet()));
		transferDTO.setUsedItems(getTransferRepository().findUsedItems(processId));
		
		return transferDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editStorageTransfer(StorageTransfer transfer) {
		//check used items amounts don't exceed the storage amounts
		dao.editProcessEntity(transfer);
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

	public List<ProcessItemInventoryRow> getInventoryByPo(Integer poCodeId) {		
		return getInventory(null, poCodeId);		
	}
	
	public List<ProcessItemInventoryRow> getInventoryByItem(Integer itemId) {		
		return getInventory(itemId, null);
	}
	
	private List<ProcessItemInventoryRow> getInventory(Integer itemId, Integer poCodeId) {
		List<ProcessItemInventoryRow> processItemRows = 
				getInventoryRepository().findInventoryProcessItem(null, itemId, poCodeId);	
		
		List<StorageInventoryRow> storageRows = getInventoryRepository()
				.findInventoryStorage(null, processItemRows.stream().map(ProcessItemInventoryRow::getId).collect(Collectors.toList()));
		
		Map<Integer, List<StorageInventoryRow>> storageMap = storageRows.stream()
				.collect(Collectors.groupingBy(StorageInventoryRow::getProcessItemId, Collectors.toList()));	
		
		processItemRows.forEach(pi -> pi.setStorageForms(storageMap.get(pi.getId())));
		
		return processItemRows;
		
	}
}
