/**
 * 
 */
package com.avc.mis.beta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.dao.ReadOnlyDAO;
import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.UserMessage;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.utilities.UserAware;

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
	
	@Autowired ReadOnlyDAO dao;
	
	@Autowired private ProcessInfoRepository processRepository;
	@Autowired Orders orders;
	
	/**
	 * Get messages for logged in user.
	 * @return List of messages for the current user including the subject process information.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	public List<UserMessageDTO> getAllMessages() {		
		return getProcessRepository().findAllMessagesByUser(dao.getCurrentUserId());
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
			default:
		}
		return null;
	}

	
}
