/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithPoCode;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.repositories.StorageRelocationRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class StorageRelocations {
	
	@Autowired private ProcessInfoDAO dao;
		
	@Autowired private StorageRelocationRepository relocationRepository;

	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void addStorageRelocation(StorageRelocation relocation) {
		relocation.setProcessType(dao.getProcessTypeByValue(ProcessName.STORAGE_RELOCATION));
		dao.setStorageMovesProcessItem(relocation.getStorageMovesGroups());
		dao.addPoProcessEntity(relocation);
		dao.checkUsedInventoryAvailability(relocation);
		dao.setRelocationPoWeights(relocation);
		dao.setUsedProcesses(relocation);
		//check if storage moves match the amounts of the used item
		dao.checkRelocationBalance(relocation);
	}
	
	public StorageRelocationDTO getStorageRelocation(int processId) {
		StorageRelocationDTO relocationDTO = new StorageRelocationDTO();
		relocationDTO.setGeneralProcessInfo(getRelocationRepository()
				.findGeneralProcessInfoByProcessId(processId, StorageRelocation.class)
				.orElseThrow(
						()->new IllegalArgumentException("No storage relocation with given process id")));
		relocationDTO.setPoProcessInfo(getRelocationRepository()
				.findPoProcessInfoByProcessId(processId, StorageRelocation.class)
				.orElseThrow(
						()->new IllegalArgumentException("No po code for given process id")));
		relocationDTO.setStorageMovesGroups(
				CollectionItemWithGroup.getFilledGroups(
						getRelocationRepository()
						.findStorageMovesWithGroup(processId)));
		relocationDTO.setItemCounts(
				CollectionItemWithGroup.getFilledGroups(
						getRelocationRepository()
						.findItemCountWithAmount(processId)));
		return relocationDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editStorageRelocation(StorageRelocation relocation) {
		dao.setStorageMovesProcessItem(relocation.getStorageMovesGroups());
		
		dao.checkRemovingUsedProduct(relocation);
		
		dao.editPoProcessEntity(relocation);
		
		dao.checkUsedInventoryAvailability(relocation);
		dao.setUsedProcesses(relocation);
		List<ItemAmountWithPoCode> usedPos = dao.setRelocationPoWeights(relocation);
		dao.checkDAGmaintained(usedPos, relocation.getId());

		dao.checkUsingProcesessConsistency(relocation);
		dao.checkRelocationBalance(relocation);
	}
		
	
	
}
