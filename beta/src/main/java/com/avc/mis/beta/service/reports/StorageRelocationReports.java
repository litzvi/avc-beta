/**
 * 
 */
package com.avc.mis.beta.service.reports;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.StorageRelocation;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class StorageRelocationReports {

	@Autowired private ProductionProcessReports processReportsReader;

	public List<ProcessRow> getStorageRelocations() {
		return getStorageRelocationsByPoCode(null, null);
	}
	
	public List<ProcessRow> getStorageRelocations(ProductionFunctionality productionFunctionality) {
		return getStorageRelocationsByPoCode(null, productionFunctionality);
	}
	
	public List<ProcessRow> getStorageRelocationsByPoCode(Integer poCodeId, ProductionFunctionality productionFunctionality) {
		return getProcessReportsReader().getProcessesByTypeAndPoCode(StorageRelocation.class, ProcessName.STORAGE_RELOCATION, poCodeId, productionFunctionality, true);
	}
	
}
