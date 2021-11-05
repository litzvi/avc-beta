/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessDAO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.collection.ProcessFileDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.repositories.QCRepository;
import com.avc.mis.beta.service.report.QualityCheckReports;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class QualityChecks {
	
	@Autowired private ProcessDAO dao;

	@Autowired private QCRepository qcRepository;
	
	@Autowired private ProcessReader processReader;
	
				
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addCashewReceiptCheck(QualityCheckDTO check) {
		check.setProcessName(ProcessName.CASHEW_RECEIPT_QC);
//		if(check.getProductionLine() == null || 
//				qcRepository.findFunctionalityByProductionLine(check.getProductionLine().getId()) != ProductionFunctionality.QUALITY_CONTROL_CHECK) {
//			throw new IllegalStateException("QC check has to have a Production Line with ProductionFunctionality.QUALITY_CONTROL_CHECK");
//		}
		return dao.addPoProcessEntity(check, QualityCheck::new);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addRoastedCashewCheck(QualityCheckDTO check) {
		check.setProcessName(ProcessName.ROASTED_CASHEW_QC);
		return dao.addPoProcessEntity(check, QualityCheck::new);
	}
	
	public QualityCheckDTO getQcByProcessId(int processId) {
		QualityCheckDTO qualityCheckDTO = new QualityCheckDTO();
		qualityCheckDTO.setGeneralProcessInfo(getQcRepository()
				.findGeneralProcessInfoByProcessId(processId, QualityCheck.class)
				.orElseThrow(
						()->new IllegalArgumentException("No quality check with given process id")));
		
		List<ProcessFileDTO> processFiles = getQcRepository().findProcessFiles(processId);
		if(processFiles != null && !processFiles.isEmpty())
			qualityCheckDTO.setProcessFiles(processFiles);
		
		qualityCheckDTO.setPoProcessInfo(getQcRepository()
				.findPoProcessInfoByProcessId(processId, QualityCheck.class)
				.orElseThrow(
						()->new IllegalArgumentException("No po code with given process id")));
		qualityCheckDTO.setQualityCheckInfo(getQcRepository().findQualityCheckInfo(processId));

		getProcessReader().setProcessWithProductCollections(qualityCheckDTO);
		qualityCheckDTO.setTestedItems(getQcRepository().findCheckItemsById(processId));
		return qualityCheckDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public void editCheck(QualityCheckDTO check) {
//		if(check.getProductionLine() == null || 
//				qcRepository.findFunctionalityByProductionLine(check.getProductionLine().getId()) != ProductionFunctionality.QUALITY_CONTROL_CHECK) {
//			throw new IllegalStateException("QC check has to have a Production Line with ProductionFunctionality.QUALITY_CONTROL_CHECK");
//		}
		dao.editGeneralProcessEntity(check, QualityCheck::new);
	}

	public CashewStandardDTO getCashewStatndard(Integer itemId, String standardOrganization) {
		CashewStandardDTO standard = getQcRepository().findCashewStandard(itemId, standardOrganization);
		return standard;
	}

	//----------------------------Duplicate in QualityCheckReports - Should remove------------------------------------------
	
	@Autowired private QualityCheckReports qualityCheckReports;

	public List<CashewQcRow> getRawQualityChecks() {
		return getQualityCheckReports().getRawQualityChecks();
	}
	
	public List<CashewQcRow> getRawQualityChecksByPoCode(@NonNull Integer poCodeId) {
		return getQualityCheckReports().getRawQualityChecksByPoCode(poCodeId);
	}
	
	public List<CashewQcRow> getRoastedQualityChecks() {
		return getQualityCheckReports().getRoastedQualityChecks();
	}
	
	public List<CashewQcRow> getRoastedQualityChecksByPoCode(@NonNull Integer poCodeId) {
		return getQualityCheckReports().getRoastedQualityChecksByPoCode(poCodeId);
	}

	
}
