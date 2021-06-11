/**
 * 
 */

package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.exportdoc.InventoryExportDoc;
import com.avc.mis.beta.dto.exportdoc.SecurityExportDoc;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ContainerLoading;
import com.avc.mis.beta.repositories.ContainerLoadingRepository;
import com.avc.mis.beta.service.report.LoadingReports;

import lombok.AccessLevel;
import lombok.Getter;

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
	
	@Autowired private ProcessReader processReader;
	@Autowired private ProcessInfoReader processInfoReader;

	
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
			dao.checkUsedInventoryAvailability(loading);
			dao.setPoWeights(loading);
			dao.setUsedProcesses(loading);
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
		
		getProcessReader().setTransactionProcessCollections(loadingDTO);
		
		loadingDTO.setLoadedItems(getContainerLoadingRepository().findLoadedItems(processId));

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
		dao.checkRemovingUsedProduct(loading);
		
		dao.editTransactionProcessEntity(loading); 
		
		dao.checkUsingProcesessConsistency(loading);
		dao.checkUsedInventoryAvailability(loading);
		dao.setPoWeights(loading);
		dao.setUsedProcesses(loading);
	}

	//----------------------------Duplicate in LoadingReports - Should remove------------------------------------------

	@Autowired private LoadingReports loadingReports;

	public List<LoadingRow> getLoadings() {
		return getLoadingsByPoCode(null);
	}
	
	public List<LoadingRow> getLoadingsByPoCode(Integer poCodeId) {		
		return getLoadingReports().getLoadingsByPoCode(poCodeId);
	}
	
	public InventoryExportDoc getInventoryExportDoc(int processId) {
		return getLoadingReports().getInventoryExportDoc(processId);		
	}
	
	public SecurityExportDoc getSecurityExportDoc(int processId) {
		return getLoadingReports().getSecurityExportDoc(processId);		
	}
	

}
