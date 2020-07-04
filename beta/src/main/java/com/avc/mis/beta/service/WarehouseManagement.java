package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.StorageTransfer;
import com.avc.mis.beta.repositories.InventoryRepository;

import lombok.AccessLevel;
import lombok.Getter;

@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class WarehouseManagement {
	
	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private InventoryRepository inventoryRepository;


	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addStorageTransfer(StorageTransfer transfer) {
		transfer.setProcessType(dao.getProcessTypeByValue(ProcessName.STORAGE_TRANSFER));
		//check used items amounts don't exceed the storage amounts
		dao.addProcessEntity(transfer);
	}
	
	public StorageTransferDTO getStorageTransfer(int processId) {
		Optional<StorageTransferDTO> transfer = getInventoryRepository().findTransferDTOByProcessId(processId);
		StorageTransferDTO transferDTO = transfer.orElseThrow(
				()->new IllegalArgumentException("No storage transfer with given process id"));
		transferDTO.setProcessItems(ProcessItemDTO
				.getProcessItems(getInventoryRepository()
						.findProcessItemWithStorage(processId))
				.stream().collect(Collectors.toSet()));
		transferDTO.setUsedItems(getInventoryRepository().findUsedItems(processId));
		
		return transferDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editStorageTransfer(StorageTransfer transfer) {
		//check used items amounts don't exceed the storage amounts
		dao.editProcessEntity(transfer);
	}

	public List<ProcessItemDTO> getInventoryByPo(Integer poCodeId) {
		return ProcessItemDTO.getProcessItems(getInventoryRepository().findProcessItemWithStorageByPoCode(poCodeId));
	}
	
	
}
