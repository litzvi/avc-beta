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
import com.avc.mis.beta.entities.process.InventoryUse;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class InventoryUseReports {

	@Autowired private ProductionProcessReports processReportsReader;

	public List<ProcessRow> getInventoryUses() {
		return getInventoryUses(null);
	}
	
	public List<ProcessRow> getInventoryUses(LocalDateTime startTime, LocalDateTime endTime) {
		return getInventoryUses(null, startTime, endTime);
	}
	
	public List<ProcessRow> getInventoryUses(Integer poCodeId) {
		return getInventoryUses(poCodeId, null, null);
	}
	
	public List<ProcessRow> getInventoryUses(Integer poCodeId, LocalDateTime startTime, LocalDateTime endTime) {
		return getProcessReportsReader().getProcessesByTypeAndPoCode(InventoryUse.class, ProcessName.GENERAL_USE, poCodeId, null, true, startTime, endTime);
	}
	
}
