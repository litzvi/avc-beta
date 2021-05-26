/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.time.LocalDateTime;
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
		return getStorageRelocations(null);
	}
	
	public List<ProcessRow> getStorageRelocations(LocalDateTime startTime, LocalDateTime endTime) {
		return getStorageRelocations(null, startTime, endTime);
	}
	
	public List<ProcessRow> getStorageRelocations(ProductionFunctionality productionFunctionality) {
		return getStorageRelocationsByPoCode(null, productionFunctionality);
	}
	
	public List<ProcessRow> getStorageRelocations(ProductionFunctionality productionFunctionality, LocalDateTime startTime, LocalDateTime endTime) {
		return getStorageRelocationsByPoCode(null, productionFunctionality, startTime, endTime);
	}
	
	public List<ProcessRow> getStorageRelocationsByPoCode(Integer poCodeId, ProductionFunctionality productionFunctionality) {
		return getStorageRelocationsByPoCode(poCodeId, productionFunctionality, null, null);
	}
	
	public List<ProcessRow> getStorageRelocationsByPoCode(Integer poCodeId, ProductionFunctionality productionFunctionality, 
			LocalDateTime startTime, LocalDateTime endTime) {
		return getProcessReportsReader().getProcessesByTypeAndPoCode(
				StorageRelocation.class, ProcessName.STORAGE_RELOCATION, poCodeId, productionFunctionality, true, 
				startTime, endTime);
	}
	
}
