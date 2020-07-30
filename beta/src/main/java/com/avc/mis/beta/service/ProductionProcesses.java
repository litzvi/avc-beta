/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.repositories.ProductionProcessRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ProductionProcesses {

	@Autowired private ProcessInfoDAO dao;

	@Autowired private ProductionProcessRepository processRepository;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addProductionProcess(ProductionProcess process, ProcessName processName) {
		process.setProcessType(dao.getProcessTypeByValue(processName));
		dao.addTransactionProcessEntity(process);
	}
	
	/**
	 * Get a full storage transfer process information
	 * @param processId id of the StorageTransfer process
	 * @return StorageTransferDTO
	 */
	public ProductionProcessDTO getProductionProcess(int processId) {
		Optional<ProductionProcessDTO> process = getProcessRepository().findProductionProcessDTOById(processId);
		ProductionProcessDTO processDTO = process.orElseThrow(
				()->new IllegalArgumentException("No production process with given process id"));
		processDTO.setProcessItems(ProcessItemDTO
				.getProcessItems(getProcessRepository()
						.findProcessItemWithStorage(processId))
				.stream().collect(Collectors.toSet()));
		processDTO.setUsedItems(getProcessRepository().findUsedItems(processId));
		
		return processDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editProductionProcess(ProductionProcess process) {
		//check used items amounts don't exceed the storage amounts
		dao.editTransactionProcessEntity(process);
	}
}
