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

import com.avc.mis.beta.dto.query.ItemAmountWithLoadingReportLine;
import com.avc.mis.beta.dto.report.FinalReport;
import com.avc.mis.beta.dto.report.InventoryReportLine;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ItemQc;
import com.avc.mis.beta.dto.report.LoadingReportLine;
import com.avc.mis.beta.dto.report.ProcessStateInfo;
import com.avc.mis.beta.dto.report.ProductionReportLine;
import com.avc.mis.beta.dto.report.QcReportLine;
import com.avc.mis.beta.dto.report.ReceiptReportLine;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.repositories.InventoryRepository;
import com.avc.mis.beta.repositories.ProcessSummaryRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

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
public class ProcessSummaryReader {

	@Autowired private ProcessSummaryRepository processSummaryRepository;
	@Autowired private InventoryRepository inventoryRepository;

	public InventoryReportLine getInventorySummary(@NonNull Integer poCodeId) {
		List<ItemAmount> inventory = getInventoryRepository().findInventoryItemAmounts(false, null, ItemGroup.PRODUCT, null, poCodeId);
		
		List<ProcessStateInfo> processes = getProcessSummaryRepository().findProcessReportLines(ProcessName.PRODUCT_USE, poCodeId, false);
		int[] processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();
		Stream<ItemAmount> itemAmounts = getProcessSummaryRepository().findSummaryUsedItemAmounts(processIds, poCodeId);
		Map<ItemGroup, List<ItemAmount>> itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));


		
//		if(inventory == null || inventory.isEmpty()) {
//			return null;
//		}
		
		InventoryReportLine reportLine = new InventoryReportLine();
		reportLine.setInventory(inventory);
		reportLine.setInventoryUse(itemsMap.get(ItemGroup.PRODUCT));
		return reportLine;
	}

	
	/**
	 * Summary of Production for a given PO Code and ProcessName,
	 * Used for Final Report
	 * @param processName the process name. e.g. CASHEW_CLEANING
	 * @param poCodeId poCodeId id of PO Code, to fetch processes that process the given PO Code.
	 * @return ProductionReportLine containing summary of all productions for the given arguments.
	 */
	public ProductionReportLine getProductionSummary(ProcessName processName, Integer poCodeId) {
		
		List<ProcessStateInfo> processes = getProcessSummaryRepository().findProcessReportLines(processName, poCodeId, false);
		int[] processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();
		if(processes.isEmpty()) {
			return null;
		}
		
		ProductionReportLine reportLine = new ProductionReportLine();
		reportLine.setProcesses(processes);
		
		Stream<ItemAmount> itemAmounts = getProcessSummaryRepository().findSummaryUsedItemAmounts(processIds, poCodeId);
		Map<ItemGroup, List<ItemAmount>> itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));

		reportLine.setProductIn(itemsMap.get(ItemGroup.PRODUCT));
		reportLine.setIngredients(itemsMap.get(ItemGroup.GENERAL));
		
		itemAmounts = getProcessSummaryRepository().findSummaryProducedItemAmounts(processIds, poCodeId);
		itemsMap = itemAmounts.collect(Collectors.groupingBy(ItemAmount::getItemGroup));

		reportLine.setProductOut(itemsMap.get(ItemGroup.PRODUCT));
		reportLine.setWaste(itemsMap.get(ItemGroup.WASTE));
		reportLine.setQc(itemsMap.get(ItemGroup.QC));
		
		List<ItemAmount> producedAmounts = new ArrayList<>();
		if(reportLine.getProductOut() != null) {
			int[] productItemsIds = reportLine.getProductOut().stream().mapToInt(i -> i.getItem().getId()).toArray();
			processes = getProcessSummaryRepository().findProcessReportLines(ProcessName.STORAGE_RELOCATION, poCodeId, false);
			processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();
			for(int i=0; i < processIds.length; i++) {
				producedAmounts.addAll(getProcessSummaryRepository().findProductCountItemAmountsByProcessId(processIds[i], productItemsIds));
			}
			reportLine.setProductCount(producedAmounts);						
		}
		
		return reportLine;
	}
	
	public ReceiptReportLine getReceiptSummary(Integer poCodeId) {
		List<ProcessStateInfo> processes = getProcessSummaryRepository().findProcessReportLines(ProcessName.CASHEW_RECEIPT, poCodeId, false);
		int[] processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();

		if(processes.isEmpty()) {
			return null;
		}
		
		ReceiptReportLine reportLine = new ReceiptReportLine();
		reportLine.setProcesses(processes);
		
		Stream<ItemAmount> itemAmounts = getProcessSummaryRepository().findSummaryProducedItemAmounts(processIds, poCodeId);
		reportLine.setReceived(itemAmounts.collect(Collectors.toList()));
		
		List<ItemAmount> countAmounts = new ArrayList<>();
		if(reportLine.getReceived() != null) {
			int[] productItemsIds = reportLine.getReceived().stream().mapToInt(i -> i.getItem().getId()).toArray();
			processes = getProcessSummaryRepository().findProcessReportLines(ProcessName.STORAGE_RELOCATION, poCodeId, false);
			processIds = processes.stream().mapToInt(ProcessStateInfo::getId).toArray();
			for(int i=0; i < processIds.length; i++) {
				countAmounts.addAll(getProcessSummaryRepository().findProductCountItemAmountsByProcessId(processIds[i], productItemsIds));
			}
			reportLine.setProductCount(countAmounts);						
		}

		return reportLine;
	}
	
	public List<QcReportLine> getQcSummary(ProcessName processName, Integer poCodeId) {
		
		List<QcReportLine> lines = getProcessSummaryRepository().findCashewQCReportLines(processName, poCodeId, QcCompany.AVC_LAB, false);
		
		
		int[] processIds = lines.stream().mapToInt(QcReportLine::getId).toArray();
		if(lines == null || lines.isEmpty()) {
			return null;
		}
		
		Stream<ItemQc> itemQcs = getProcessSummaryRepository().findCashewQcItems(processIds);
		Map<Integer, List<ItemQc>> itemsMap = itemQcs.collect(Collectors.groupingBy(ItemQc::getProcessId));
		
		for(QcReportLine line: lines) {
			line.setItemQcs(itemsMap.get(line.getId()));
		}
		return lines;
	}

	public List<LoadingReportLine> getLoadingSummary(Integer poCodeId) {	
		
		List<ItemAmountWithLoadingReportLine> lines = getProcessSummaryRepository().findLoadingsItemsAmounts(poCodeId, false);		
		return CollectionItemWithGroup.getFilledGroups(lines);
	}
	
	public FinalReport getFinalReport(@NonNull Integer poCodeId) {
		FinalReport report = new FinalReport();
		report.setInventory(getInventorySummary(poCodeId));
		report.setReceipt(getReceiptSummary(poCodeId));
		report.setReceiptQC(getQcSummary(ProcessName.CASHEW_RECEIPT_QC, poCodeId));
		report.setCleaning(getProductionSummary(ProcessName.CASHEW_CLEANING, poCodeId));
		report.setRoasting(getProductionSummary(ProcessName.CASHEW_ROASTING, poCodeId));
		report.setRoastQC(getQcSummary(ProcessName.ROASTED_CASHEW_QC, poCodeId));
		report.setToffee(getProductionSummary(ProcessName.CASHEW_TOFFEE, poCodeId));
		report.setPacking(getProductionSummary(ProcessName.PACKING, poCodeId));
		report.setLoadings(getLoadingSummary(poCodeId));
		
		return report;
	}

	

}
