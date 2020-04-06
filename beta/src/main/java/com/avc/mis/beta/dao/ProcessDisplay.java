/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.UserMessageDTO;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ProcessType;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(readOnly = true)
public class ProcessDisplay extends DAO {
	
	@Autowired Orders orders;
	
	public List<UserMessageDTO> getAllMessages(Integer userId) {
		return getProcessRepository().findAllMessagesByUser(userId);
	}
	
	public ProductionProcess getProcess(Integer processId, String processTypeName) {
		ProcessName processName = Enum.valueOf(ProcessName.class, processTypeName);
		switch(processName) {
		case CASHEW_ORDER:
		case GENERAL_ORDER:
			orders.getOrderByProcessId(processId);
			break;
			default:
		}
		return null;
	}
}
