/**
 * 
 */

package com.avc.mis.beta.service;

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
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.repositories.ContainerLoadingRepository;

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

	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	public List<LoadingRow> getLoadings() {
		List<LoadingRow> loadingRows = getContainerLoadingRepository().findContainerLoadings();
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getContainerLoadingRepository()
				.findAllUsedItems()
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ProductionProcessWithItemAmount>> loadedMap = getContainerLoadingRepository()
				.findAllLoadedItems()
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		Map<Integer, List<ContainerPoItemRow>> usedByPoMap = getContainerLoadingRepository()
				.findLoadedTotals(null).stream()
				.collect(Collectors.groupingBy(ContainerPoItemRow::getId));
		for(LoadingRow row: loadingRows) {
			row.setUsedItems(usedMap.get(row.getId()));
			row.setLoadedItems(loadedMap.get(row.getId()));
			row.setLoadedTotals(usedByPoMap.get(row.getId()));
		}		
		
		return loadingRows;
	}
	
	public List<LoadingRow> getLoadingsByPoCode(@NonNull Integer poCodeId) {
		Map<Integer, List<ProductionProcessWithItemAmount>> usedMap = getContainerLoadingRepository()
				.findAllUsedItemsByPoCode(poCodeId)
				.collect(Collectors.groupingBy(ProductionProcessWithItemAmount::getId));
		List<LoadingRow> loadingRows = getContainerLoadingRepository()
				.findContainerLoadingsByProcessIds(
						usedMap.keySet().stream().mapToInt(Integer::intValue).toArray());
		for(LoadingRow row: loadingRows) {
			row.setUsedItems(usedMap.get(row.getId()));
		}		
		
		return loadingRows;
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
		//using save rather than persist in case POid was assigned by user
		dao.addEntityWithFlexibleGenerator(loading.getShipmentCode());
				
		dao.addTransactionProcessEntity(loading); 
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
		Optional<ContainerLoadingDTO> loading = getContainerLoadingRepository().findContainerLoadingDTOById(processId);
		ContainerLoadingDTO loadingDTO = loading.orElseThrow( ()->new IllegalArgumentException("No container loading with given process id"));
		loadingDTO.setLoadedItems(getContainerLoadingRepository().findLoadedItems(processId));
		loadingDTO.setUsedItemGroups(
				UsedItemsGroupDTO.getUsedItemsGroups(
						getContainerLoadingRepository()
						.findUsedItemsWithGroup(processId)));

		return loadingDTO; 
	}

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
		doc.setLoadedTotals(getContainerLoadingRepository().findLoadedTotals(processId));
		
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
