/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ReadOnlyDAO;
import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.ProcessManagementDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.PoProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ProcessBasic;
import com.avc.mis.beta.dto.values.UserBasic;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.repositories.ProcessInfoRepository;

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
public class ProcessInfoReader {
	
	@Autowired private ReadOnlyDAO dao;
	
	@Autowired private ProcessInfoRepository processInfoRepository;
	@Autowired private Orders orders;
	@Autowired private Receipts orderReceipts;
	@Autowired private QualityChecks qualityChecks;
	@Autowired private Samples samples;
	@Autowired private WarehouseManagement warehouseManagement;
	@Autowired private ProductionProcesses productionProcesses;
	
	
	/**
	 * Gets a ProcessManagement that contains a user to be notified, 
	 * for a given process and the type of approval required.
	 * @param id of requested ProcessManagement
	 * @return ProcessManagement with the given id.
	 */
	public ProcessManagement getProcessTypeAlert(Integer id) {
		return processInfoRepository.findProcessManagementById(id);
	}
	
	/**
	 * Gets all the alerts requirements set in the system
	 * @return nested Map by Process name with List of ProcessManagementDTO, that contains user and approval type.
	 */
	public Map<ProcessName, Map<UserBasic, List<BasicValueEntity<ProcessManagement>>>> getAllProcessTypeAlerts() {
		List<ProcessManagementDTO> processTypeAlerts = processInfoRepository.findAllProcessManagements();
		
		return processTypeAlerts.stream()
			.collect(Collectors.groupingBy(ProcessManagementDTO::getProcessName, 
					Collectors.groupingBy(ProcessManagementDTO::getUser, 
							Collectors.mapping(ProcessManagementDTO::getApprovalType, Collectors.toList()))));
//		return processTypeAlerts.stream().collect(Collectors.groupingBy(ProcessManagementDTO::getProcessName));
		
	}
	
	/**
	 * Get messages for logged in user.
	 * @return List of messages for the current user including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<UserMessageDTO> getAllMessages() {		
		return getProcessInfoRepository().findAllMessagesByUser(dao.getCurrentUserId());
	}
	
	@Deprecated
	public List<UserMessageDTO> getAllUserMessages(Integer userId) {		
		return getProcessInfoRepository().findAllMessagesByUser(userId);
	}
	
	/**
	 * Get new - not read - messages for a logged in user.
	 * @return List of new  messages for the given user including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<UserMessageDTO> getAllNewMessages() {
		return getProcessInfoRepository().findAllMessagesByUserAndLable(dao.getCurrentUserId(), 
				new MessageLabel[] {MessageLabel.NEW});
	}
	
	/**
	 * Get all unattended approval tasks required for the current logged in user.
	 * @return List of unattended approval tasks including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<ApprovalTaskDTO> getAllRequiredApprovals() {
		return getProcessInfoRepository().findAllRequiredApprovalsByUser(dao.getCurrentUserId(), 
				new DecisionType[] {DecisionType.EDIT_NOT_ATTENDED, DecisionType.NOT_ATTENDED});
	}
	
	/**
	 * Get all approval tasks for the current logged in user.
	 * @return List of approval tasks including the subject process information for current user.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<ApprovalTaskDTO> getAllApprovals() {
		return getProcessInfoRepository().findAllApprovalsByUser(dao.getCurrentUserId());
	}
		
	/**
	 * Gets current user's management privileges for given processType
	 * @param processName the processName for the process type
	 * @return List of ManagementType enums
	 */
	public List<ManagementType> getUserManagementTypes(ProcessName processName) {
		return getProcessInfoRepository().findUserProcessPrivilige(processName, dao.getCurrentUserId());
	}
	
	/**
	 * Gets current user's management privileges for all processType
	 * @return Map with List of ManagementType enums for every ProcessName
	 */
	public Map<ProcessName, List<ManagementType>> getAllUserManagementTypes() {
		List<ProcessManagementDTO> processManagements = getProcessInfoRepository().findAllUserProcessPrivilige(dao.getCurrentUserId());
		return processManagements.stream().collect(Collectors.groupingBy(ProcessManagementDTO::getProcessName, 
				Collectors.mapping(ProcessManagementDTO::getManagementType, Collectors.toList())));
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
		case CASHEW_CLEANING:
		case CASHEW_ROASTING:
		case PACKING:
			return productionProcesses.getProductionProcess(processId);
			default:
		}
		return null;
	}
	
	/**
	 * Gets a list of processes done for the given po code
	 * @param poCodeId id of PoCode
	 * @return List of ProcessBasic
	 */
	public List<ProcessBasic> getAllProcessesByPo(@NonNull Integer poCodeId) {
		return getProcessInfoRepository().findAllProcessesByPo(poCodeId);
	}
	
	
	/**
	 * Gets a list of processes of the given types (by name) for the given po code.
	 * @param poCodeId id of PoCode
	 * @param processNames all process types (by name) to get
	 * @return List of ProcessBasic
	 */
	public List<ProcessBasic> getAllProcessesByPoAndName(@NonNull Integer poCodeId, Set<ProcessName> processNames) {
		return getProcessInfoRepository().findAllProcessesByPoAndName(poCodeId, processNames);
	}

	
}
