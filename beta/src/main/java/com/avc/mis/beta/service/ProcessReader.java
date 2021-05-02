/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.PoProcessDTO;
import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.process.ProcessWithProductDTO;
import com.avc.mis.beta.dto.process.TransactionProcessDTO;
import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.process.collection.WeightedPoDTO;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Used for accessing and updating information notifying about processes (rather then process data itself). 
 * e.g. messages and approvals.
 * Basically for handling entities who are instance of {@link com.avc.mis.beta.entities.ProcessInfoEntity}
 * 
 * @author Zvi
 *
 */
@Service
@Getter(value = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ProcessReader {
	
	@Autowired private ProcessInfoRepository processInfoRepository;
	
	@Autowired private Orders orders;
	@Autowired private Receipts orderReceipts;
	@Autowired private QualityChecks qualityChecks;
	@Autowired private Samples samples;
	@Autowired private WarehouseManagement warehouseManagement;
	@Autowired private ProductionProcesses productionProcesses;
	@Autowired private Loading loading;
	
	
	/**
	 * Fetches and sets all information for the given Transaction Process DTO by it's id.
	 * @param processDTO TransactionProcessDTO with id set inside
	 */
	void setTransactionProcessCollections(TransactionProcessDTO<ProcessItemDTO> processDTO) {
		processDTO.setUsedItemGroups(
				CollectionItemWithGroup.getFilledGroups(
						getProcessInfoRepository()
						.findUsedItemsWithGroup(processDTO.getId())));
		
		setProcessWithProductCollections(processDTO);
		
	}
	
	/**
	 * Fetches and sets all information for the given Process With Product DTO by it's id.
	 * @param processDTO ProcessWithProductDTO with id set inside
	 */
	void setProcessWithProductCollections(ProcessWithProductDTO<ProcessItemDTO> processDTO) {
		processDTO.setProcessItems(
				CollectionItemWithGroup.getFilledGroups(
						getProcessInfoRepository()
						.findProcessItemWithStorage(processDTO.getId())));

		setPoProcessCollections(processDTO);
		
	}

	/**
	 * Fetches and sets all information for the given PO Process DTO by it's id.
	 * @param processDTO PoProcessDTO with id set inside
	 */
	void setPoProcessCollections(PoProcessDTO processDTO) {
		List<WeightedPoDTO> weightedPos = getProcessInfoRepository().findWeightedPos(processDTO.getId());
		if(!weightedPos.isEmpty())
			processDTO.setWeightedPos(weightedPos);
	}
	
	/**
	 * Generic method for getting any type of process without needing to find the specific method 
	 * created for that special process type. e.g. order, receiving etc.
	 * @param processId
	 * @param processTypeName
	 * @return GeneralProcessDTO with information of the requested process.
	 */
	public PoProcessDTO getProcess(int processId, ProcessName processName) {
//		ProcessName processName = Enum.valueOf(ProcessName.class, processTypeName);
		switch(processName) {
		case CASHEW_ORDER:
		case GENERAL_ORDER:
			return orders.getOrderByProcessId(processId);
		case CASHEW_RECEIPT:
		case GENERAL_RECEIPT:
			return orderReceipts.getReceiptByProcessId(processId);
		case CASHEW_RECEIPT_QC:
		case SUPPLIER_QC:
		case VINA_CONTROL_QC:
		case SAMPLE_QC:
			return qualityChecks.getQcByProcessId(processId);
		case SAMPLE_RECEIPET:
			return samples.getSampleReceiptByProcessId(processId);
		case STORAGE_TRANSFER:
			return warehouseManagement.getStorageTransfer(processId);
		case STORAGE_RELOCATION:
			return warehouseManagement.getStorageRelocation(processId);
		case CASHEW_CLEANING:
		case CASHEW_ROASTING:
		case PACKING:
			return productionProcesses.getProductionProcess(processId);
		case CONTAINER_LOADING:
			return loading.getLoading(processId);
			default:
		}
		return null;
	}
	
	/**
	 * Gets a list of processes done for the given po code
	 * @param poCodeId id of PoCode
	 * @return List of ProcessBasic
	 */
	public List<ProcessBasic<GeneralProcess>> getAllProcessesByPo(@NonNull Integer poCodeId) {
		return getProcessInfoRepository().findAllProcessesByPo(poCodeId);
	}
	
	/**
	 * @param processId id of the process
	 * @return ProcessBasic for the process
	 */
	public ProcessBasic<GeneralProcess> getProcessesBasic(@NonNull Integer processId) {
		return getProcessInfoRepository().findProcessBasic(processId);
	}	
	
	/**
	 * Gets a list of processes of the given types (by name) for the given po code.
	 * @param poCodeId id of PoCode
	 * @param processNames all process types (by name) to get
	 * @return List of ProcessBasic
	 */
	public List<ProcessBasic<PoProcess>> getAllProcessesByPoAndName(@NonNull Integer poCodeId, Set<ProcessName> processNames) {
		return getProcessInfoRepository().findAllProcessesByPoAndName(poCodeId, processNames);
	}
	
	/**
	 * Gets a list of ALL processes
	 * @return List of ProcessBasic
	 */
	public List<ProcessBasic<PoProcess>> getAllProcesses() {
		return getProcessInfoRepository().findAllProcessesByPoAndName();
	}	
}
