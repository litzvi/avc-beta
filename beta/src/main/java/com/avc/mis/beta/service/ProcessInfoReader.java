/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.ReadOnlyDAO;
import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.ProcessAlertDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.values.UserBasic;
import com.avc.mis.beta.entities.data.ProcessAlert;
import com.avc.mis.beta.entities.enums.ApprovalType;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.repositories.ProcessInfoRepository;

import lombok.AccessLevel;
import lombok.Getter;

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
	
	@Autowired private ProcessInfoRepository processRepository;
	@Autowired private Orders orders;
	@Autowired private OrderReceipts orderReceipts;
	@Autowired private QualityChecks qualityChecks;
	@Autowired private Samples samples;
	
	
	/**
	 * Gets a ProcessAlert that contains a user to be notified, 
	 * for a given process and the type of approval required.
	 * @param id of requested ProcessAlert
	 * @return ProcessAlert with the given id.
	 */
	public ProcessAlert getProcessTypeAlert(Integer id) {
		return processRepository.findProcessTypeAlertById(id);
	}
	
	/**
	 * Gets all the alerts requirements set in the system
	 * @return nested Map by Process name, Approval type with List of Users.
	 */
	public Map<ProcessName, Map<ApprovalType, List<ProcessAlertDTO>>> getAllProcessTypeAlerts() {
		List<ProcessAlertDTO> processTypeAlerts = processRepository.findAllProcessAlerts();
		return processTypeAlerts.stream()
			.collect(Collectors.groupingBy(ProcessAlertDTO::getProcessName, 
					Collectors.groupingBy(ProcessAlertDTO::getApprovalType, Collectors.toList())));
//							Collectors.mapping(ProcessAlertDTO::getUser, Collectors.toList()))));
//		return processTypeAlerts.stream().collect(Collectors.groupingBy(ProcessAlertDTO::getProcessName));
		
	}
	
	/**
	 * Get messages for logged in user.
	 * @return List of messages for the current user including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<UserMessageDTO> getAllMessages() {		
		return getProcessRepository().findAllMessagesByUser(dao.getCurrentUserId());
	}
	
	@Deprecated
	public List<UserMessageDTO> getAllUserMessages(Integer userId) {		
		return getProcessRepository().findAllMessagesByUser(userId);
	}
	
	/**
	 * Get new - not read - messages for a logged in user.
	 * @return List of new  messages for the given user including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<UserMessageDTO> getAllNewMessages() {
		return getProcessRepository().findAllMessagesByUserAndLable(dao.getCurrentUserId(), 
				new MessageLabel[] {MessageLabel.NEW});
	}
	
	/**
	 * Get all unattended approval tasks required for the current logged in user.
	 * @return List of unattended approval tasks including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<ApprovalTaskDTO> getAllRequiredApprovals() {
		return getProcessRepository().findAllRequiredApprovalsByUser(dao.getCurrentUserId(), 
				new DecisionType[] {DecisionType.EDIT_NOT_ATTENDED, DecisionType.NOT_ATTENDED});
	}
	
	/**
	 * Get all approval tasks for the current logged in user.
	 * @return List of approval tasks including the subject process information for current user.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<ApprovalTaskDTO> getAllApprovals() {
		return getProcessRepository().findAllApprovalsByUser(dao.getCurrentUserId());
	}
	
	/**
	 * Generic method for getting any type of process without needing to find the specific method 
	 * created for that special process type. e.g. order, receiving etc.
	 * @param processId
	 * @param processTypeName
	 * @return ProductionProcessDTO with information of the requested process.
	 */
	public ProductionProcessDTO getProcess(int processId, String processTypeName) {
		ProcessName processName = Enum.valueOf(ProcessName.class, processTypeName);
		switch(processName) {
		case CASHEW_ORDER:
		case GENERAL_ORDER:
			return orders.getOrderByProcessId(processId);
		case CASHEW_ORDER_RECEIPT:
		case CASHEW_RECEIPT:
			return orderReceipts.getReceiptByProcessId(processId);
		case CASHEW_RECEIPT_QC:
		case SUPPLIER_QC:
		case VINA_CONTROL_QC:
		case SAMPLE_QC:
			return qualityChecks.getQcByProcessId(processId);
		case SAMPLE_RECEIPET:
			return samples.getSampleReceiptByProcessId(processId);
			default:
		}
		return null;
	}

	
}
