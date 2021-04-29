/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ReadOnlyDAO;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ProcessStateInfo;
import com.avc.mis.beta.dto.report.ProductionReportLine;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.repositories.ProcessSummaryRepository;
import com.avc.mis.beta.repositories.ProductionProcessRepository;
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
public class ProcessReportsReader {

	@Autowired private TransactionProcessRepository<TransactionProcess<ProcessItem>> transactionProcessRepository;
	@Autowired private RelocationRepository relocationRepository;

	
	/**
	 * Gets list of process rows, filled with used, produced and count information for a given process name.
	 * Optional filters are PO Code, Production Functionality and if to retrive cancelled processes.
	 */
	public <T extends PoProcess> List<ProcessRow> getProcessesByTypeAndPoCode(
			@NonNull Class<T> processClass, @NonNull ProcessName processName, 
			Integer poCodeId, ProductionFunctionality functionality, boolean cancelled) {
		List<ProcessRow> processRows = getTransactionProcessRepository().findProcessByType(processName, poCodeId, functionality, cancelled);
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
