/**
 * 
 */
package com.avc.mis.beta.service.report;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.repositories.QCRepository;

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
public class QualityCheckReports {

	@Autowired private QCRepository qcRepository;
	
	public List<CashewQcRow> getRawQualityChecks() {
		return getRawQualityChecks(null, null);
	}

	public List<CashewQcRow> getRawQualityChecks(LocalDateTime startTime, LocalDateTime endTime) {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.CASHEW_RECEIPT_QC,
				ProcessName.SUPPLIER_QC,
				ProcessName.VINA_CONTROL_QC,
				ProcessName.SAMPLE_QC}, null, startTime, endTime);
	}
	
	public List<CashewQcRow> getRawQualityChecksByPoCode(@NonNull Integer poCodeId) {
		return getRawQualityChecksByPoCode(poCodeId, null, null);
	}
	
	public List<CashewQcRow> getRawQualityChecksByPoCode(@NonNull Integer poCodeId, LocalDateTime startTime, LocalDateTime endTime) {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.CASHEW_RECEIPT_QC,
				ProcessName.SUPPLIER_QC,
				ProcessName.VINA_CONTROL_QC,
				ProcessName.SAMPLE_QC}, poCodeId, startTime, endTime);
	}
	
	public List<CashewQcRow> getRoastedQualityChecks() {
		return getRoastedQualityChecks(null, null);
	}
	
	public List<CashewQcRow> getRoastedQualityChecks(LocalDateTime startTime, LocalDateTime endTime) {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.ROASTED_CASHEW_QC}, null, startTime, endTime);
	}
	
	public List<CashewQcRow> getRoastedQualityChecksByPoCode(@NonNull Integer poCodeId) {
		return getRoastedQualityChecksByPoCode(poCodeId, null, null);
	}
	
	public List<CashewQcRow> getRoastedQualityChecksByPoCode(@NonNull Integer poCodeId, LocalDateTime startTime, LocalDateTime endTime) {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.ROASTED_CASHEW_QC}, poCodeId, startTime, endTime);
	}
	
}
