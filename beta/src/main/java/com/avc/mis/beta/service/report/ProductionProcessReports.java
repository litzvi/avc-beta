/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.repositories.RelocationRepository;
import com.avc.mis.beta.repositories.TransactionProcessRepository;

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
public class ProductionProcessReports {

	@Autowired private TransactionProcessRepository<TransactionProcess<ProcessItem>> transactionProcessRepository;
	@Autowired private RelocationRepository relocationRepository;

	public List<ProcessRow> getProductionProcessesByType(ProcessName processName) {
		return getProductionProcessesByType(processName, null, null);
	}
	
	public List<ProcessRow> getProductionProcessesByType(ProcessName processName, 
			LocalDateTime startTime, LocalDateTime endTime) {
		return getProductionProcessesByTypeAndPoCode(processName, null, startTime, endTime);
	}
	
	public List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
		return getProductionProcessesByTypeAndPoCode(processName, poCodeId, null, null);
	}
	
	public List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId, 
			LocalDateTime startTime, LocalDateTime endTime) {
		return getProcessesByTypeAndPoCode(ProductionProcess.class, processName, poCodeId, null, true, startTime, endTime);
	}
	
	public <T extends PoProcess> List<ProcessRow> getProcessesByTypeAndPoCode(
			@NonNull Class<T> processClass, ProcessName processName, 
			Integer poCodeId, ProductionFunctionality functionality, boolean cancelled) {
		return getProcessesByTypeAndPoCode(processClass, processName, poCodeId, functionality, cancelled, null, null);		
	}
	
	/**
	 * Gets list of process rows, filled with used, produced and count information for a given process name.
	 * Optional filters are PO Code, Production Functionality and if to retrive cancelled processes.
	 */
	public <T extends PoProcess> List<ProcessRow> getProcessesByTypeAndPoCode(
			@NonNull Class<T> processClass, @NonNull ProcessName processName, 
			Integer poCodeId, ProductionFunctionality functionality, boolean cancelled, 
			LocalDateTime startTime, LocalDateTime endTime) {
		List<ProcessRow> processRows = getTransactionProcessRepository().findProcessByType(
				processClass, processName, poCodeId, functionality, cancelled, startTime, endTime);
		int[] processIds = processRows.stream().mapToInt(ProcessRow::getId).toArray();
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = null;
		Map<Integer, List<ProductionProcessWithItemAmount>> countMap = null;
		if(TransactionProcess.class.isAssignableFrom(processClass)) {
			usedMap = getTransactionProcessRepository()
					.findAllUsedItemsByProcessIds(processIds)
					.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		}
		else if(StorageRelocation.class.isAssignableFrom(processClass)) {
			usedMap = getRelocationRepository()
					.findAllMovedItemsByProcessIds(processIds)
					.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
			countMap = getTransactionProcessRepository()
					.findAllItemsCountsByProcessIds(processIds)
					.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));

		}
		
		Map<Integer, List<ProductionProcessWithItemAmount>> producedMap = null;
		if(ProcessWithProduct.class.isAssignableFrom(processClass)) {
			producedMap = getTransactionProcessRepository()
					.findAllProducedItemsByProcessIds(processIds)
					.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		}
		
		for(ProcessRow row: processRows) {
			if(usedMap != null)
				row.setUsedItems(usedMap.get(row.getId()));
			if(producedMap != null)
				row.setProducedItems(producedMap.get(row.getId()));
			if(countMap != null)
			row.setItemCounts(countMap.get(row.getId()));
		}	
		return processRows;
	}
	
}
