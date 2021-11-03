/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ProcessInfoDAO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.repositories.ProductionProcessRepository;
import com.avc.mis.beta.service.interfaces.ProductionProcessService;
import com.avc.mis.beta.service.report.ProductionProcessReports;

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
	@Autowired private ProductionProcessReports productionProcessReports;
		
	
	@Override
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	public Integer addProductionProcess(ProductionProcessDTO process, ProcessName processName) {
		process.setProcessName(processName);
//		process.setProcessType(dao.getProcessTypeByValue(processName));
		Integer processId = dao.addTransactionProcessEntity(process, ProductionProcess::new);	
		dao.checkTransactionUsedInventoryAvailability(processId);
		dao.setTransactionPoWeights(processId);
		dao.setTransactionUsedProcesses(processId);
		
		return processId;
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
		
	@Transactional(rollbackFor = Throwable.class, readOnly = false, isolation = Isolation.SERIALIZABLE)
	@Override
	public void editProductionProcess(ProductionProcessDTO process) {
		dao.checkRemovingUsedProduct(process);
		
		dao.editTransactionProcessEntity(process, ProductionProcess::new);		
		dao.checkUsingProcesessConsistency(process);
		dao.checkTransactionUsedInventoryAvailability(process.getId());
		dao.setTransactionPoWeights(process.getId());
		dao.setTransactionUsedProcesses(process.getId());
	}

	//----------------------------Duplicate in ProductionProcessReports - Should remove------------------------------------------
	
	
	@Override
	public List<ProcessRow> getProductionProcessesByType(ProcessName processName) {
		return getProductionProcessesByTypeAndPoCode(processName, null);
	}
	
	@Override
	public List<ProcessRow> getProductionProcessesByTypeAndPoCode(ProcessName processName, Integer poCodeId) {
		return getProductionProcessReports().getProductionProcessesByTypeAndPoCode(processName, poCodeId);
	}

}
