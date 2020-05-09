/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.entities.data.ProcessTypeAlert;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.ProductionProcess;
import com.avc.mis.beta.entities.values.ProcessType;

/**
 * Spring repository for accessing all notification information and requirements of production processes.
 * 
 * @author Zvi
 *
 */
public interface ProcessInfoRepository extends BaseRepository<ProductionProcess> {

	@Query("select a from ProcessTypeAlert a where a.processType = ?1")
	List<ProcessTypeAlert> findProcessTypeAlerts(ProcessType processType);

	@Query("select p.approvals from ProductionProcess p where p = ?1")
	List<ApprovalTask> findProcessApprovals(ProductionProcess process);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
				+ "m.id, m.version, c, m.description, p.id, p.processType, m.createdDate, pr.name, prm.name,  m.label) "
			+ "from UserMessage m "
			+ "join m.process p "
			+ "join p.poCode c "
			+ "join m.user u "
			+ "join u.person pr "
			+ "join m.modifiedBy um "
			+ "join um.person prm "
			+ "where u.id = ?1 "
			+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessagesByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, c, pa.description, p.id, p.processType, pa.createdDate, "
			+ "pr.name, prm.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
		+ "join pa.process p "
		+ "join p.poCode c "
		+ "join pa.user u "
		+ "join u.person pr "
		+ "join pa.modifiedBy um "
		+ "join um.person prm "
		+ "where pa.decision in :decisions "
			+ "and u.id = :userId "
		+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findAllRequiredApprovalsByUser(Integer userId, DecisionType[] decisions);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, c, pa.description, p.id, p.processType, pa.createdDate, "
			+ "pr.name, prm.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
		+ "join pa.process p "
		+ "join p.poCode c "
		+ "join pa.user u "
		+ "join u.person pr "
		+ "join pa.modifiedBy um "
		+ "join um.person prm "
		+ "where u.id = :userId "
		+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findAllApprovalsByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
			+ "m.id, m.version, c, m.description, p.id, p.processType, m.createdDate, pr.name, prm.name, m.label) "
		+ "from UserMessage m "
		+ "join m.process p "
		+ "join p.poCode c "
		+ "join m.user u "
		+ "join u.person pr "
		+ "join m.modifiedBy um "
		+ "join um.person prm "
		+ "where u.id = ?1 "
			+ "and m.label in ?2 "
		+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessagesByUserAndLable(Integer userId, MessageLabel[] lables);

}
