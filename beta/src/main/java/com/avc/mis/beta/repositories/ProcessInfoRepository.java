/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.ProcessManagementDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.entities.process.ApprovalTask;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * Spring repository for accessing all notification information and requirements of production processes.
 * 
 * @author Zvi
 *
 */
public interface ProcessInfoRepository extends BaseRepository<ProductionProcess> {

//	@Query("select a "
//			+ "from ProcessManagement a "
//			+ "join fetch a.user "
//			+ "where a.processType = ?1")
//	List<ProcessManagement> findProcessTypeAlerts(ProcessType processType);
	
	@Query("select a "
			+ "from ProcessManagement a "
			+ "join fetch a.user "
			+ "join ProductionProcess p "
				+ "on p.processType = a.processType "
			+ "where p.id = ?1")
	List<ProcessManagement> findProcessTypeAlertsByProcess(Integer processId);


	@Query("select p.approvals from ProductionProcess p where p.id = ?1")
	List<ApprovalTask> findProcessApprovals(Integer processId);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
				+ "m.id, m.version, c.id, t.code, m.description, p.id, pt.processName, m.createdDate, pr.name, prm.name,  m.label) "
			+ "from UserMessage m "
				+ "left join m.process p "
					+ "left join p.processType pt "
					+ "left join p.poCode c "
						+ "left join c.contractType t "
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
			+ "from ProcessManagement a "
//				+ "join fetch a.user "
//				+ "join fetch a.processType "
			+ "where a.id = :id")
	ProcessManagement findProcessManagementById(Integer id);

	@Query("select new com.avc.mis.beta.dto.data.ProcessManagementDTO(a.id, t.processName, "
				+ "u.id, u.version, u.username, a.managementType) "
			+ "from ProcessManagement a "
			+ "join a.user u "
			+ "join a.processType t ")
	List<ProcessManagementDTO> findAllProcessManagements();


//	@Modifying
//	@Query("update ProcessLifeCycle c "
//			+ "join c.process p "
//			+ "join ProcessManagement m "
//					+ "on p.processType = m.processType "
//				+ "join m.user u "
//			+ "set c.status = :status "
//			+ "where p.id = :processId and "
//				+ "u.id = :currentUserId and "
//				+ "m.managementType = com.avc.mis.beta.entities.enums.ManagementType.MANAGER ")
//	int updateLifeCycleStatus(RecordStatus status, Integer processId, Integer currentUserId);
	
	@Query("select c "
			+ "from ProcessLifeCycle c "
				+ "join c.process p "
				+ "join ProcessManagement m "
					+ "on p.processType = m.processType "
					+ "join m.user u "
			+ "where p.id = :processId and "
				+ "u.id = :currentUserId and "
				+ "m.managementType = com.avc.mis.beta.entities.enums.ManagementType.MANAGER ")
	Optional<ProcessLifeCycle> findProcessLifeCycleManagerByUser(Integer processId, Integer currentUserId);


	@Query("select c.status "
			+ "from ProcessLifeCycle c "
				+ "join c.process p "
			+ "where p.id = :processId ")
	RecordStatus findProcessLifeCycleStatus(Integer processId);

}
