/**
 * 
 */

package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ContainerLoading;

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

	@Deprecated
	@Autowired private DeletableDAO deletableDAO;

	/**
	 * Adds a new container loading
	 * 
	 * @param loading container loading with all required details
	 * @throws IllegalArgumentException if used items don't match current inventory.
	 */
	@Transactional(rollbackFor = Throwable.class, readOnly = false) 
	public void addLoading(ContainerLoading loading) {
		loading.setProcessType(dao.getProcessTypeByValue(ProcessName.CONTAINER_LOADING)); 
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
//	public ContainerLoadingDTO getLoading(int processId) {
//		Optional<ContainerLoadingDTO> loading = getContainerLoadingRepository().findLoadingById(processId);
//		ContainerLoadingDTO loadingDTO = loading.orElseThrow( ()->new IllegalArgumentException("No container loading with given process id"));
//		loadingDTO.setBookedContainers(getShipmentBookingRepository().findBookedContainersByProcessId(processId));
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
}
