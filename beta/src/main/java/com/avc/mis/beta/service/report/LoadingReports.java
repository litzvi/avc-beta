/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.exportdoc.ContainerPoItemRow;
import com.avc.mis.beta.dto.exportdoc.ExportInfo;
import com.avc.mis.beta.dto.exportdoc.InventoryExportDoc;
import com.avc.mis.beta.dto.exportdoc.SecurityExportDoc;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.repositories.ContainerLoadingRepository;
import com.avc.mis.beta.service.report.row.CashewBaggedInventoryRow;
import com.avc.mis.beta.service.report.row.CashewExportReportRow;

import lombok.AccessLevel;
import lombok.Getter;

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
		return getLoadings(null, null);
	}

	public List<LoadingRow> getLoadings(LocalDateTime startTime, LocalDateTime endTime) {
		return getLoadingsByPoCode(null, startTime, endTime);
	}
	
	public List<LoadingRow> getLoadingsByPoCode(Integer poCodeId) {
		return getLoadingsByPoCode(poCodeId, null, null);
	}
	
	public List<LoadingRow> getLoadingsByPoCode(Integer poCodeId, LocalDateTime startTime, LocalDateTime endTime) {
		List<LoadingRow> loadingRows = getContainerLoadingRepository().findContainerLoadings(poCodeId, true, startTime, endTime);
		int[] processIds = loadingRows.stream().mapToInt(LoadingRow::getId).toArray();
//		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getContainerLoadingRepository()
//				.findAllUsedItemsByProcessIds(processIds)
//				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ContainerPoItemRow>> usedByPoMap = getContainerLoadingRepository()
				.findLoadedTotals(processIds)
				.stream()
				.collect(Collectors.groupingBy(ContainerPoItemRow::getId));
		for(LoadingRow row: loadingRows) {
//			row.setUsedItems(usedMap.get(row.getId()));
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

	public List<CashewExportReportRow> getCashewExportReportRows(LocalDateTime startTime, LocalDateTime endTime) {
		return getContainerLoadingRepository().findCashewExportReportRows(startTime, endTime);	
	}
}
