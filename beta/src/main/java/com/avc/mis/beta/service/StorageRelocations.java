/**
 * 
 */
package com.avc.mis.beta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.repositories.StorageRelocationRepository;

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
	
	@Autowired private ProcessDAO dao;
	
	@Autowired private ProcessReader processReader;
	@Autowired private StorageRelocationRepository relocationRepository;

	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addStorageRelocation(StorageRelocationDTO relocation) {
		relocation.setProcessName(ProcessName.STORAGE_RELOCATION);
		return dao.addRelocationProcessEntity(relocation, StorageRelocation::new);
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
		getProcessReader().setRelocationProcessCollections(relocationDTO);

		return relocationDTO;
	}
	
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editStorageRelocation(StorageRelocationDTO relocation) {
		dao.editRelocationProcessEntity(relocation, StorageRelocation::new);
	}
		
	
	
}
