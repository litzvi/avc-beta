/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.embedable.PoProcessInfo;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ProductionReportLine;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.StorageRelocation;
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
	
	@Autowired private ProcessInfoReader processInfoReader;
	
	public List<ProcessRow> getProductionProcessesByType(ProcessName processName) {
		return getProductionProcessesByTypeAndPoCode(processName, null);
	}
	
	/**
	 * @param cashewCleaning
	 * @param poCodeId
	 * @return
	 */
	public List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
		List<ProcessRow> processRows = getProcessRepository().findProcessByType(processName, poCodeId, true);
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
	
	public ProductionReportLine getProductionSummary(ProcessName processName, Integer poCodeId) {
		List<ProcessRow> processRows = getProcessRepository().findProcessByType(processName, poCodeId, false);
		int[] processIds = processRows.stream().mapToInt(ProcessRow::getId).toArray();

		if(processRows.isEmpty()) {
			return null;
		}
		
		ProductionReportLine reportLine = new ProductionReportLine();
		reportLine.setProcesses(processRows.stream());
//		reportLine.setDates(processRows.stream().map(r -> r.getRecordedTime().toLocalDate()).collect(Collectors.toSet()));
		
		Stream<ItemAmount> itemAmounts = getProcessRepository().findSummaryUsedItemAmounts(processIds);
		Map<ItemGroup, List<ItemAmount>> itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));

		reportLine.setProductIn(itemsMap.get(ItemGroup.PRODUCT));
		reportLine.setIngredients(itemsMap.get(ItemGroup.GENERAL));
		
		itemAmounts = getProcessRepository().findSummaryProducedItemAmounts(processIds);
		itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));

		reportLine.setProductOut(itemsMap.get(ItemGroup.PRODUCT));
		reportLine.setWaste(itemsMap.get(ItemGroup.WASTE));
		reportLine.setQc(itemsMap.get(ItemGroup.QC));
		
		List<ItemAmount> producedAmounts = null;
		if(reportLine.getProductOut() != null) {
			int[] productItemsIds = reportLine.getProductOut().stream().mapToInt(i -> i.getItem().getId()).toArray();
			processRows = getProcessRepository().findProcessByType(ProcessName.STORAGE_RELOCATION, poCodeId, false);
			processIds = processRows.stream().mapToInt(ProcessRow::getId).toArray();
			for(int i=0; i < processIds.length && (producedAmounts == null || producedAmounts.isEmpty()); i++) {
				producedAmounts = getProcessRepository().findProductCountItemAmountsByProcessId(processIds[i], productItemsIds);
			}
			reportLine.setProductCount(producedAmounts);						
		}
		
		return reportLine;
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
		ProductionProcessDTO processDTO = new ProductionProcessDTO();
		processDTO.setGeneralProcessInfo(getProcessRepository()
				.findGeneralProcessInfoByProcessId(processId, ProductionProcess.class)
				.orElseThrow(
						()->new IllegalArgumentException("No production process with given process id")));
		processDTO.setPoProcessInfo(getProcessRepository()
				.findPoProcessInfoByProcessId(processId).orElse(null));
		
		getProcessInfoReader().setTransactionProcessCollections(processDTO);

		return processDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editProductionProcess(ProductionProcess process) {
//		if(true)
//			throw new NullPointerException();
		//check used items amounts don't exceed the storage amounts
		dao.editTransactionProcessEntity(process);
	}

	
}
