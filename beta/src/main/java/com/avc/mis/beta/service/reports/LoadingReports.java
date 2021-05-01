/**
 * 
 */
package com.avc.mis.beta.service.reports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ReadOnlyDAO;
import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.doc.ContainerPoItemRow;
import com.avc.mis.beta.dto.doc.ExportInfo;
import com.avc.mis.beta.dto.doc.InventoryExportDoc;
import com.avc.mis.beta.dto.doc.SecurityExportDoc;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ProcessStateInfo;
import com.avc.mis.beta.dto.report.ProductionReportLine;
import com.avc.mis.beta.dto.view.ContainerArrivalRow;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.repositories.ContainerArrivalRepository;
import com.avc.mis.beta.repositories.ContainerLoadingRepository;
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
public class LoadingReports {
	
	@Autowired private ContainerLoadingRepository containerLoadingRepository;


	public List<LoadingRow> getLoadings() {
		return getLoadingsByPoCode(null);
	}
	
	public List<LoadingRow> getLoadingsByPoCode(Integer poCodeId) {
		List<LoadingRow> loadingRows = getContainerLoadingRepository().findContainerLoadings(poCodeId, true);
		int[] processIds = loadingRows.stream().mapToInt(LoadingRow::getId).toArray();
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getContainerLoadingRepository()
				.findAllUsedItemsByProcessIds(processIds)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ContainerPoItemRow>> usedByPoMap = getContainerLoadingRepository()
				.findLoadedTotals(processIds)
				.stream()
				.collect(Collectors.groupingBy(ContainerPoItemRow::getId));
		for(LoadingRow row: loadingRows) {
			row.setUsedItems(usedMap.get(row.getId()));
			row.setLoadedTotals(usedByPoMap.get(row.getId()));
		}		
		
		return loadingRows;
	}
	
	public InventoryExportDoc getInventoryExportDoc(int processId) {
		InventoryExportDoc doc = new InventoryExportDoc();
		Optional<ExportInfo> optInfo = getContainerLoadingRepository().findInventoryExportDocById(processId);
		doc.setExportInfo(optInfo.orElseThrow( ()->new IllegalArgumentException("No container loading with given process id")));
		doc.setLoadedTotals(getContainerLoadingRepository().findLoadedTotals(new int[] {processId}));
		
		return doc; 
		
	}
	
	public SecurityExportDoc getSecurityExportDoc(int processId) {
		SecurityExportDoc doc = new SecurityExportDoc();
		Optional<ExportInfo> optInfo = getContainerLoadingRepository().findInventoryExportDocById(processId);
		doc.setExportInfo(optInfo.orElseThrow( ()->new IllegalArgumentException("No container loading with given process id")));
		doc.setLoadedStorages(getContainerLoadingRepository().findLoadedStorages(processId));
		
		return doc; 
		
	}

	
}
