/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.exportdoc.ContainerPoItemRow;
import com.avc.mis.beta.dto.exportdoc.ExportInfo;
import com.avc.mis.beta.dto.exportdoc.InventoryExportDoc;
import com.avc.mis.beta.dto.exportdoc.SecurityExportDoc;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
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
				.findLoadedTotals(processIds, poCodeId)
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
		doc.setLoadedTotals(getContainerLoadingRepository().findLoadedTotals(new int[] {processId}, null));
		
		return doc; 
		
	}
	
	public SecurityExportDoc getSecurityExportDoc(int processId) {
		SecurityExportDoc doc = new SecurityExportDoc();
		Optional<ExportInfo> optInfo = getContainerLoadingRepository().findInventoryExportDocById(processId);
		doc.setExportInfo(optInfo.orElseThrow( ()->new IllegalArgumentException("No container loading with given process id")));
		doc.setLoadedStorages(getContainerLoadingRepository().findLoadedStorages(processId));
		
		return doc; 
		
	}

	public List<CashewExportReportRow> getCashewExportReportRowsOld(LocalDateTime startTime, LocalDateTime endTime) {
		return getContainerLoadingRepository().findCashewExportReportRows(false, null, null, 
				startTime, endTime, Sort.by("recordedTime", "id", "item.itemGroup", "item.id"));	
	}
	
	public List<CashewExportReportRow> getCashewExportReportRows(LocalDateTime startTime, LocalDateTime endTime) {
		List<CashewExportReportRow> rows = getContainerLoadingRepository().findCashewExportReportRows(false, null, null, 
				startTime, endTime, Sort.by("recordedTime", "id", "item.itemGroup", "item.id", "w_po.ordinal"));
//		startTime, endTime, Sort.by("recordedTime", "id", "item.itemGroup", "item.id", "used_p.id", "w_po.ordinal"));
		
		Map<Pair<Integer, Integer>, List<CashewExportReportRow>> map = rows.stream().collect(Collectors.groupingBy(
				i -> Pair.of(i.getShipmentCode().getId(), i.getItem().getId())));
		
		for(Pair<Integer, Integer> key: map.keySet()) {
			List<CashewExportReportRow> keyRows = map.get(key);
			BigDecimal remainder = BigDecimal.ZERO;
			for(CashewExportReportRow r: keyRows) {
				BigDecimal boxQuantity = r.getBoxQuantityReal();
				BigDecimal[] divideAndRemainder = boxQuantity.divideAndRemainder(BigDecimal.ONE);
				r.setBoxQuantity(divideAndRemainder[0]);
				remainder = remainder.add(divideAndRemainder[1]);
			}
			remainder = remainder.setScale(0, RoundingMode.HALF_DOWN);
			CashewExportReportRow rowToAddRemainder = keyRows.get(0);
			rowToAddRemainder.setBoxQuantity(rowToAddRemainder.getBoxQuantity().add(remainder));
			
		}
		
		return rows;
	}
	
	public List<CashewBaggedInventoryRow> getCashewBaggedExportReportRows(ItemGroup itemGroup, ProductionUse[] productionUses, 
			LocalDateTime startTime, LocalDateTime endTime) {
		boolean checkProductionUses = (productionUses != null);
		List<CashewExportReportRow> exportReportRows = 
				getContainerLoadingRepository().findCashewExportReportRows(checkProductionUses, productionUses, itemGroup,
						startTime, endTime, Sort.by("item.brand", "item.code", "item.id"));
		
		Map<Integer, List<CashewExportReportRow>> map = exportReportRows
				.stream()
				.collect(Collectors.groupingBy(i -> i.getItem().getId(), 
						LinkedHashMap::new, 
						Collectors.toList()));
		
		List<CashewBaggedInventoryRow> baggedRows = new ArrayList<>();
		for(int key: map.keySet()) {
			CashewBaggedInventoryRow row = new CashewBaggedInventoryRow(map.get(key).stream().findAny().get());
			row.setTotalAmount(map.get(key).stream()
					.map(CashewBaggedInventoryRow::getTotalAmount)
					.reduce(AmountWithUnit::add).get()
					.setScale(MeasureUnit.SUM_DISPLAY_SCALE));
			row.setBoxQuantityReal(map.get(key).stream()
					.map(CashewBaggedInventoryRow::getBoxQuantityReal)
					.reduce(BigDecimal::add).get()
					.setScale(MeasureUnit.SUM_DISPLAY_SCALE));
			row.setWeightCoefficient(BigDecimal.ONE);
			baggedRows.add(row);
		}
		
		return baggedRows;
		
	}
}
