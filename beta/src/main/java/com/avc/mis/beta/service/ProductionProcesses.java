/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.repositories.ProductionProcessRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

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
	
	public List<ProcessRow> getProductionProcessesByType(ProcessName processName) {
		return getProductionProcessesByTypeAndPoCode(processName, null);
	}
	
	/**
	 * @param cashewCleaning
	 * @param poCodeId
	 * @return
	 */
	public List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
		List<ProcessRow> processRows = getProcessRepository().findProcessByType(processName, poCodeId);
		int[] processIds = processRows.stream().mapToInt(ProcessRow::getId).toArray();
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getProcessRepository()
				.findAllUsedItemsByProcessIds(processIds)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ProductionProcessWithItemAmount>> producedMap = getProcessRepository()
				.findAllProducedItemsByProcessIds(processIds)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		for(ProcessRow row: processRows) {
			row.setUsedItems(usedMap.get(row.getId()));
			row.setProducedItems(producedMap.get(row.getId()));
		}		
		return processRows;
	}
	
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
						.findProcessItemWithStorage(processId)));
		processDTO.setUsedItemGroups(
				UsedItemsGroupDTO.getUsedItemsGroups(
						getProcessRepository()
						.findUsedItemsWithGroup(processId)));
		
		return processDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editProductionProcess(ProductionProcess process) {
		System.out.println("production edit called");
		//check used items amounts don't exceed the storage amounts
		dao.editTransactionProcessEntity(process);
	}

	
}
