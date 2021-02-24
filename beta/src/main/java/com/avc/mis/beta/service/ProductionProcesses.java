/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.avc.mis.beta.dto.processinfo.WeightedPoDTO;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ProcessStateInfo;
import com.avc.mis.beta.dto.report.ProductionReportLine;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
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
	
	/**
	 * @param processName the process name. e.g. CASHEW_CLEANING
	 * @return List of ProcessRow for given type of process
	 */
	public List<ProcessRow> getProductionProcessesByType(ProcessName processName) {
		return getProductionProcessesByTypeAndPoCode(processName, null);
	}
	
	/**
	 * @param processName the process name. e.g. CASHEW_CLEANING
	 * @param poCodeId id of po code, to fetch processes that process the given po code.
	 * If poCodeId argument is null, so will get a list of all process of the given type.
	 * In case of a mixed process will only show the given po code in the process row.
	 * @return List of ProcessRow for all processes that match the given arguments
	 */
	private List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
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
//		List<ProcessRow> processRows = getProcessRepository().findProcessByType(processName, poCodeId, false);
		
		List<ProcessStateInfo> processes = getProcessRepository().findProcessReportLines(processName, poCodeId, false);
		int[] processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();

		if(processes.isEmpty()) {
			return null;
		}
		
		ProductionReportLine reportLine = new ProductionReportLine();
		reportLine.setProcesses(processes);
		
		Stream<ItemAmount> itemAmounts = getProcessRepository().findSummaryUsedItemAmounts(processIds);
		Map<ItemGroup, List<ItemAmount>> itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));

		reportLine.setProductIn(itemsMap.get(ItemGroup.PRODUCT));
		reportLine.setIngredients(itemsMap.get(ItemGroup.GENERAL));
		
		itemAmounts = getProcessRepository().findSummaryProducedItemAmounts(processIds, poCodeId);
		itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));

		reportLine.setProductOut(itemsMap.get(ItemGroup.PRODUCT));
		reportLine.setWaste(itemsMap.get(ItemGroup.WASTE));
		reportLine.setQc(itemsMap.get(ItemGroup.QC));
		
		List<ItemAmount> producedAmounts = null;
		if(reportLine.getProductOut() != null) {
			int[] productItemsIds = reportLine.getProductOut().stream().mapToInt(i -> i.getItem().getId()).toArray();
//			List<ProcessRow> processRows = getProcessRepository().findProcessByType(ProcessName.STORAGE_RELOCATION, poCodeId, false);
			processes = getProcessRepository().findProcessReportLines(ProcessName.STORAGE_RELOCATION, poCodeId, false);
			processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();
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
	
	public ProductionProcessDTO getProductionProcessWithAvilableInventory(
			int processId, ItemGroup group, ProductionUse[] productionUses, Integer itemId) {
		
		ProductionProcessDTO processDTO = getProductionProcess(processId);
		
		List<Integer> poCodeIds = Arrays.asList(Optional.ofNullable(processDTO.getPoCode()).map(i -> i.getId()).orElse(null));		
		if(processDTO.getWeightedPos() != null) {
			poCodeIds.addAll(processDTO.getWeightedPos().stream()
					.map(WeightedPoDTO::getPoCode)
					.filter(i -> i != null)
					.map(PoCodeBasic::getId).collect(Collectors.toList()));
		}
		
		getProcessInfoReader().setAvailableInventory(processDTO, group, productionUses, itemId, poCodeIds.toArray(new Integer[poCodeIds.size()]));

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
