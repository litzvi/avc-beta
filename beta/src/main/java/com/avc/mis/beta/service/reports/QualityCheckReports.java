/**
 * 
 */
package com.avc.mis.beta.service.reports;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessWithProduct;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.TransactionProcess;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.repositories.QCRepository;
import com.avc.mis.beta.repositories.RelocationRepository;
import com.avc.mis.beta.repositories.TransactionProcessRepository;

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
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.CASHEW_RECEIPT_QC,
				ProcessName.SUPPLIER_QC,
				ProcessName.VINA_CONTROL_QC,
				ProcessName.SAMPLE_QC}, null);
	}
	
	public List<CashewQcRow> getRawQualityChecksByPoCode(@NonNull Integer poCodeId) {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.CASHEW_RECEIPT_QC,
				ProcessName.SUPPLIER_QC,
				ProcessName.VINA_CONTROL_QC,
				ProcessName.SAMPLE_QC}, poCodeId);
	}
	
	public List<CashewQcRow> getRoastedQualityChecks() {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.ROASTED_CASHEW_QC}, null);
	}
	
	public List<CashewQcRow> getRoastedQualityChecksByPoCode(@NonNull Integer poCodeId) {
		return getQcRepository().findCashewQualityChecks(new ProcessName[] {
				ProcessName.ROASTED_CASHEW_QC}, poCodeId);
	}
	
}
