/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.repositories.ProductionProcessRepository;
import com.avc.mis.beta.serviceinterface.ProductionProcessService;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author zvi
 *
 */
@Service @Primary
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ProductionProcesses implements ProductionProcessService {

	@Autowired private ProcessInfoDAO dao;

	@Autowired private ProductionProcessRepository processRepository;
	
	@Autowired private ProcessReader processReader;
	@Autowired private ProcessInfoReader processInfoReader;
	@Autowired private ProcessReportsReader processReportsReader;
	@Autowired private ProcessSummaryReader processSummaryReader;
		
	@Override
	public List<ProcessRow> getProductionProcessesByType(ProcessName processName) {
		return getProductionProcessesByTypeAndPoCode(processName, null);
	}
	
	@Override
	public List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
		return getProcessReportsReader().getProcessesByTypeAndPoCode(ProductionProcess.class, processName, poCodeId, null, true);
	}
		
	@Override
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addProductionProcess(ProductionProcess process, ProcessName processName) {
		process.setProcessType(dao.getProcessTypeByValue(processName));
		dao.addTransactionProcessEntity(process);	
	}
	
	@Override
	public ProductionProcessDTO getProductionProcess(int processId) {
		ProductionProcessDTO processDTO = new ProductionProcessDTO();
		processDTO.setGeneralProcessInfo(getProcessRepository()
				.findGeneralProcessInfoByProcessId(processId, ProductionProcess.class)
				.orElseThrow(
						()->new IllegalArgumentException("No production process with given process id")));
		processDTO.setPoProcessInfo(getProcessRepository()
				.findPoProcessInfoByProcessId(processId, ProductionProcess.class).orElse(null));		
		getProcessReader().setTransactionProcessCollections(processDTO);		
		return processDTO;
	}
		
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	@Override
	public void editProductionProcess(ProductionProcess process) {
		//check used items amounts don't exceed the storage amounts
		dao.editTransactionProcessEntity(process);
	}

	
}
