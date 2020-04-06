/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.UserMessageDTO;
import com.avc.mis.beta.entities.data.UserEntity;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(readOnly = true)
public class ProcessDisplay extends DAO {
	
	
	public List<UserMessageDTO> getAllMessages(Integer userId) {
		return getProcessRepository().findAllMessagesByUser(userId);
	}
}
