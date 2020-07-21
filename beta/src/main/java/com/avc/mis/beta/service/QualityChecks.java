/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.view.RawQcRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.repositories.QCRepository;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class QualityChecks {
	
	@Autowired private ProcessInfoDAO dao;

	@Autowired private QCRepository qcRepository;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	
	public List<RawQcRow> getRawQualityChecks() {
		return getQcRepository().findRawQualityChecks();
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceiptCheck(QualityCheck check) {
		check.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT_QC));
		dao.addProcessEntity(check);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewSampleCheck(QualityCheck check) {
		check.setProcessType(dao.getProcessTypeByValue(ProcessName.SAMPLE_QC));
		dao.addProcessEntity(check);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewSupplierCheck(QualityCheck check) {
		check.setProcessType(dao.getProcessTypeByValue(ProcessName.SUPPLIER_QC));
		dao.addProcessEntity(check);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewVinaControlCheck(QualityCheck check) {
		check.setProcessType(dao.getProcessTypeByValue(ProcessName.VINA_CONTROL_QC));
		dao.addProcessEntity(check);
	}
	
	public QualityCheckDTO getQcByProcessId(int processId) {
//		Optional<QualityCheck> result = getQcRepository().findQcByProcessId(processId);
//		QualityCheck qualityCheck = result.orElseThrow(
//				()->new IllegalArgumentException("No quality check with given process id"));
//		QualityCheckDTO qualityCheckDTO = new QualityCheckDTO(qualityCheck);
		Optional<QualityCheckDTO> check = getQcRepository().findQcDTOByProcessId(processId);
		QualityCheckDTO qualityCheckDTO = check.orElseThrow(
				()->new IllegalArgumentException("No quality check with given process id"));
		qualityCheckDTO.setProcessItems(getQcRepository().findProcessItemWithStorage(processId));
		qualityCheckDTO.setTestedItems(getQcRepository().findCheckItemsById(processId));
		
		return qualityCheckDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editCheck(QualityCheck check) {
		dao.editProcessEntity(check);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Deprecated
	public void removeCheck(int checkId) {
		getDeletableDAO().permenentlyRemoveEntity(QualityCheck.class, checkId);
	}	

	public CashewStandardDTO getCashewStatndard(Integer itemId, String standardOrganization) {
		CashewStandardDTO standard = getQcRepository().findCashewStandard(itemId, standardOrganization);
//		CashewStandardDTO standardDTO = standard
//				.orElseThrow(()->new IllegalArgumentException("No cashew standard for given item from given organization"));
		return standard;
	}
}
