/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.entities.process.Receipt;
import com.avc.mis.beta.repositories.QcRepository;

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

	@Autowired private QcRepository qcRepository;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceiptCheck(QualityCheck check) {
		check.setProcessType(getQcRepository().findProcessTypeByValue(ProcessName.KERNEL_RECEIPT_QC));
		dao.addProcessEntity(check);
	}
	
	public QualityCheckDTO getQcByProcessId(int processId) {
		Optional<QualityCheckDTO> check = getQcRepository().findQcByProcessId(processId);
		QualityCheckDTO qualityCheckDTO = check.orElseThrow(
				()->new IllegalArgumentException("No quality check with given process id"));
		qualityCheckDTO.setCheckItems(getQcRepository().findCheckItemsById(processId));
		
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
}