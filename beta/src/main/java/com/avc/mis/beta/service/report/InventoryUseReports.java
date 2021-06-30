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

	public List<ProcessRow> getInventoryUses(ProcessName processName) {
		return getInventoryUses(processName, null);
	}
	
	public List<ProcessRow> getInventoryUses(ProcessName processName, LocalDateTime startTime, LocalDateTime endTime) {
		return getInventoryUses(processName, null, startTime, endTime);
	}
	
	public List<ProcessRow> getInventoryUses(ProcessName processName, Integer poCodeId) {
		return getInventoryUses(processName, poCodeId, null, null);
	}
	
	public List<ProcessRow> getInventoryUses(ProcessName processName, Integer poCodeId, LocalDateTime startTime, LocalDateTime endTime) {
		return getProcessReportsReader().getProcessesByTypeAndPoCode(InventoryUse.class, processName, poCodeId, null, true, startTime, endTime);
	}
	
}
