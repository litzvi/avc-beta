/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.entities.data.ProcessTypeAlert;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ProcessType;

/**
 * @author Zvi
 *
 */
public interface ProcessRepository extends BaseRepository<ProductionProcess> {

	@Query("select a from ProcessTypeAlert a where a.processType = ?1")
	List<ProcessTypeAlert> findProcessTypeAlerts(ProcessType processType);

	@Query("select p.approvals from ProductionProcess p where p = ?1")
	List<ApprovalTask> findProcessApprovals(ProductionProcess process);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
				+ "m.id, m.version, m.title, p.id, p.processType, m.createdDate, m.label) "
			+ "from UserMessage m "
			+ "join m.process p "
			+ "join m.user u "
			+ "where u.id = ?1 ")
	List<UserMessageDTO> findAllMessagesByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, pa.title, p.id, p.processType, pa.createdDate, pa.decision, pa.processVersion) "
		+ "from ApprovalTask pa "
		+ "join pa.process p "
		+ "join pa.user u "
		+ "where u.id = ?1 ")
	List<ApprovalTaskDTO> findAllRequiredApprovalsByUser(Integer userId);

}
