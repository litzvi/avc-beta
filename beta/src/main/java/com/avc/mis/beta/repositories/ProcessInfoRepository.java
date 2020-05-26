/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.ProcessAlertDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.entities.data.ProcessAlert;
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

//	@Query("select a "
//			+ "from ProcessAlert a "
//			+ "join fetch a.user "
//			+ "where a.processType = ?1")
//	List<ProcessAlert> findProcessTypeAlerts(ProcessType processType);
	
	@Query("select a "
			+ "from ProcessAlert a "
			+ "join fetch a.user "
			+ "join ProductionProcess p "
				+ "on p.processType = a.processType "
			+ "where p.id = ?1")
	List<ProcessAlert> findProcessTypeAlertsByProcess(Integer processId);


	@Query("select p.approvals from ProductionProcess p where p = ?1")
	List<ApprovalTask> findProcessApprovals(ProductionProcess process);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
				+ "m.id, m.version, c.id, t.code, m.description, p.id, pt.processName, m.createdDate, pr.name, prm.name,  m.label) "
			+ "from UserMessage m "
				+ "join m.process p "
					+ "join p.processType pt "
					+ "join p.poCode c "
						+ "join c.contractType t "
				+ "join m.user u "
					+ "join u.person pr "
				+ "join m.modifiedBy um "
					+ "join um.person prm "
			+ "where u.id = ?1 "
			+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessagesByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, c.id, t.code, pa.description, p.id, pt.processName, pa.createdDate, "
			+ "pr.name, prm.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
			+ "join pa.process p "
				+ "join p.processType pt "
				+ "join p.poCode c "
					+ "join c.contractType t "
			+ "join pa.user u "
				+ "join u.person pr "
			+ "join pa.modifiedBy um "
				+ "join um.person prm "
		+ "where pa.decision in :decisions "
			+ "and u.id = :userId "
		+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findAllRequiredApprovalsByUser(Integer userId, DecisionType[] decisions);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, c.id, t.code, pa.description, p.id, pt.processName, pa.createdDate, "
			+ "pr.name, prm.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
		+ "join pa.process p "
			+ "join p.processType pt "
		+ "join p.poCode c "
			+ "join c.contractType t "
		+ "join pa.user u "
		+ "join u.person pr "
		+ "join pa.modifiedBy um "
		+ "join um.person prm "
		+ "where u.id = :userId "
		+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findAllApprovalsByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
			+ "m.id, m.version, c.id, t.code, m.description, p.id, pt.processName, m.createdDate, pr.name, prm.name, m.label) "
		+ "from UserMessage m "
		+ "join m.process p "
			+ "join p.processType pt "
		+ "join p.poCode c "
			+ "join c.contractType t "
		+ "join m.user u "
		+ "join u.person pr "
		+ "join m.modifiedBy um "
		+ "join um.person prm "
		+ "where u.id = ?1 "
			+ "and m.label in ?2 "
		+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessagesByUserAndLable(Integer userId, MessageLabel[] lables);

	@Query("select a "
			+ "from ProcessAlert a "
//				+ "join fetch a.user "
//				+ "join fetch a.processType "
			+ "where a.id = :id")
	ProcessAlert findProcessTypeAlertById(Integer id);

	@Query("select new com.avc.mis.beta.dto.data.ProcessAlertDTO(a.id, t.processName, "
				+ "u.id, u.version, u.username, a.approvalType) "
			+ "from ProcessAlert a "
			+ "join a.user u "
			+ "join a.processType t ")
	List<ProcessAlertDTO> findAllProcessAlerts();

}
