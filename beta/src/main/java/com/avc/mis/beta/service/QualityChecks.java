/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DeletableDAO;
import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.process.PoProcessDTO;
import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.report.ItemQc;
import com.avc.mis.beta.dto.report.QcReportLine;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.entities.process.QualityCheck;
import com.avc.mis.beta.repositories.QCRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

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
	
	@Autowired private ProcessInfoDAO dao;

	@Autowired private QCRepository qcRepository;
	
	@Autowired private ProcessInfoReader processInfoReader;
	
	@Deprecated
	@Autowired private DeletableDAO deletableDAO;
	
	
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
	
	public List<QcReportLine> getQcSummary(ProcessName processName, Integer poCodeId) {
		
		List<QcReportLine> lines = getQcRepository().findCashewQCReportLines(processName, poCodeId, QcCompany.AVC_LAB);
		int[] processIds = lines.stream().mapToInt(QcReportLine::getId).toArray();
//		System.out.println("lines: " + lines.size());
		if(lines == null || lines.isEmpty()) {
			return null;
		}
		
		Stream<ItemQc> itemQcs = getQcRepository().findCashewQcItems(processIds);
		Map<Integer, List<ItemQc>> itemsMap = itemQcs.collect(Collectors.groupingBy(ItemQc::getProcessId));
		
		for(QcReportLine line: lines) {
			line.setItemQcs(itemsMap.get(line.getId()));
		}
		
		
		return lines;
		
//		return CollectionItemWithGroup.getFilledGroups(lines);
	}
			
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewReceiptCheck(QualityCheck check) {
		check.setProcessType(dao.getProcessTypeByValue(ProcessName.CASHEW_RECEIPT_QC));
		dao.addGeneralProcessEntity(check);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addRoastedCashewCheck(QualityCheck check) {
		check.setProcessType(dao.getProcessTypeByValue(ProcessName.ROASTED_CASHEW_QC));
		dao.addGeneralProcessEntity(check);
	}
	
//	@Transactional(rollbackFor = Throwable.class, readOnly = false)
//	public void addCashewSampleCheck(QualityCheck check) {
//		check.setProcessType(dao.getProcessTypeByValue(ProcessName.SAMPLE_QC));
//		dao.addGeneralProcessEntity(check);
//	}
//	
//	@Transactional(rollbackFor = Throwable.class, readOnly = false)
//	public void addCashewSupplierCheck(QualityCheck check) {
//		check.setProcessType(dao.getProcessTypeByValue(ProcessName.SUPPLIER_QC));
//		dao.addGeneralProcessEntity(check);
//	}
//	
//	@Transactional(rollbackFor = Throwable.class, readOnly = false)
//	public void addCashewVinaControlCheck(QualityCheck check) {
//		check.setProcessType(dao.getProcessTypeByValue(ProcessName.VINA_CONTROL_QC));
//		dao.addGeneralProcessEntity(check);
//	}
	
	public Map<ProcessName, List<PoProcessDTO>> getAllQualityChecksByPo(@NonNull Integer poCodeId) {
		Map<ProcessName, List<PoProcessDTO>> checksMap = new HashMap<>();
		for(ProcessName processName: ProcessName.values()) {
			if(processName.name().contains("QC")) {
				checksMap.put(processName, new ArrayList<PoProcessDTO>());
			}
		}
		List<ProcessBasic> basicProcesses = getProcessInfoReader().getAllProcessesByPoAndName(poCodeId, checksMap.keySet());
		basicProcesses.forEach(p -> checksMap.get(p.getProcessName()).add(getProcessInfoReader().getProcess(p.getId(), p.getProcessName())));
		
		
		return checksMap;
		
	}
	
	public QualityCheckDTO getQcByProcessId(int processId) {
		Optional<QualityCheckDTO> check = getQcRepository().findQcDTOByProcessId(processId);
		QualityCheckDTO qualityCheckDTO = check.orElseThrow(
				()->new IllegalArgumentException("No quality check with given process id"));
		qualityCheckDTO.setProcessItems(
				CollectionItemWithGroup.getFilledGroups(
						getQcRepository()
						.findProcessItemWithStorage(processId)));
//				ProcessItemDTO.getProcessItems(getQcRepository()
//						.findProcessItemWithStorage(processId)));
		qualityCheckDTO.setTestedItems(getQcRepository().findCheckItemsById(processId));
		return qualityCheckDTO;
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editCheck(QualityCheck check) {
		dao.editGeneralProcessEntity(check);
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
