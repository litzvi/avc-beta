/**
 * 
 */

package com.avc.mis.beta.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.doc.ContainerPoItemRow;
import com.avc.mis.beta.dto.doc.ExportInfo;
import com.avc.mis.beta.dto.doc.InventoryExportDoc;
import com.avc.mis.beta.dto.doc.SecurityExportDoc;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.processinfo.WeightedPoDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithLoadingReportLine;
import com.avc.mis.beta.dto.report.LoadingReportLine;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.repositories.ContainerLoadingRepository;
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
public class Loading {

	@Autowired private ProcessInfoDAO dao;
	
	@Autowired private ContainerLoadingRepository containerLoadingRepository;
	
	@Autowired private ProcessInfoReader processInfoReader;

	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
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
	
	public List<LoadingReportLine> getLoadingSummary(Integer poCodeId) {
		
		
		List<ItemAmountWithLoadingReportLine> lines = getContainerLoadingRepository().findLoadingsItemsAmounts(poCodeId, false);
		
		return CollectionItemWithGroup.getFilledGroups(lines);
	}


	/**
	 * Adds a new container loading
	 * 
	 * @param loading container loading with all required details
	 * @throws IllegalArgumentException if used items don't match current inventory.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false) 
	public void addLoading(ContainerLoading loading) {
		loading.setProcessType(dao.getProcessTypeByValue(ProcessName.CONTAINER_LOADING)); 
		if(dao.isShippingCodeFree(loading.getShipmentCode().getId())) {
			dao.addTransactionProcessEntity(loading); 
		}
		else {
			throw new IllegalArgumentException("Shipment Code is already used for another shipping");
		}
	}

	/**
	 * Gets full details of container loading with given ContainerLoading id.
	 * 
	 * @param processId the id of ContainerLoading requested
	 * @return ContainerLoadingDTO object with container loading details
	 * @throws IllegalArgumentException if container loading for given process id
	 *                                  dosen't exist.
	 */
	public ContainerLoadingDTO getLoading(int processId) {
		ContainerLoadingDTO loadingDTO = new ContainerLoadingDTO();
		loadingDTO.setGeneralProcessInfo(getContainerLoadingRepository()
				.findGeneralProcessInfoByProcessId(processId, ContainerLoading.class)
				.orElseThrow(
						()->new IllegalArgumentException("No container loading with given process id")));
		loadingDTO.setContainerLoadingInfo(getContainerLoadingRepository().findContainerLoadingInfo(processId));
		
		getProcessInfoReader().setTransactionProcessCollections(loadingDTO);
		
		loadingDTO.setLoadedItems(getContainerLoadingRepository().findLoadedItems(processId));

		return loadingDTO; 
	}
	
//	public ContainerLoadingDTO getLoadingWithAvilableInventory(
//			int processId, ItemGroup group, ProductionUse[] productionUses, Integer itemId) {
//		
//		ContainerLoadingDTO loadingDTO = getLoading(processId);
//		
//		List<Integer> poCodeIds = Arrays.asList(Optional.ofNullable(loadingDTO.getPoCode()).map(i -> i.getId()).orElse(null));		
//		if(loadingDTO.getWeightedPos() != null) {
//			poCodeIds.addAll(loadingDTO.getWeightedPos().stream()
//					.map(WeightedPoDTO::getPoCode)
//					.filter(i -> i != null)
//					.map(PoCodeBasic::getId).collect(Collectors.toList()));
//		}
//		getProcessInfoReader().setAvailableInventory(loadingDTO, group, productionUses, itemId, poCodeIds.toArray(new Integer[poCodeIds.size()]));
//
//		return loadingDTO;
//	}

	/**
	 * Update the given ContainerLoading with the set data - Process information and
	 * remarks. Ignores changed non editable fields.
	 * 
	 * @param loading ContainerLoading updated with edited state
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false) 
	public void editLoading(ContainerLoading loading) {
		dao.editTransactionProcessEntity(loading); 
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)

	@Deprecated public void removeLoading(int loadingId) {
		getDeletableDAO().permenentlyRemoveEntity(ContainerLoading.class, loadingId);
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
