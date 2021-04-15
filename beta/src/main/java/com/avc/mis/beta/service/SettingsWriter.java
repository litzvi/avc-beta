/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.settings.UOM;

/**
 * @author Zvi
 *
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class SettingsWriter {

	@Autowired private DeletableDAO dao;
	
	@Autowired ProcessInfoReader processInfoReader;
	@Autowired ProcessInfoDAO processInfoDAO;
	
	
	public UOM editUOM(UOM uom) {
		return dao.editEntity(uom);
	}

	/**
	 * Persists or edits every UOM object in the list
	 * @param uomList
	 */
	public void mergeAll(List<UOM> uomList) {
		dao.addOrEdit(uomList);	
	}

	/**
	 * Persists all entities in the list
	 * @param entityList
	 */
	public <T extends BaseEntity> void addAll(List<T> entityList) {
		dao.addAll(entityList);
	}
	
	public <T extends BaseEntity> boolean isTableEmpty(Class<T> entityClass) {
		return dao.isTableEmpty(entityClass);	
	}

	
	public void refillAllProcessParents() {
		List<ProcessBasic<PoProcess>> processes = processInfoReader.getAllProcesses();
		for(ProcessBasic<PoProcess> processBasic: processes) {
			PoProcess process = processBasic.getProcess();
			if(process instanceof TransactionProcess) {
				processInfoDAO.setUsedProcesses((TransactionProcess)process);
			}
			if(process instanceof StorageRelocation) {
				processInfoDAO.setUsedProcesses((StorageRelocation)process);
			}
		}
	}
}
