/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ApprovalTaskDTO;
import com.avc.mis.beta.dto.data.ProcessManagementDTO;
import com.avc.mis.beta.dto.data.UserMessageDTO;
import com.avc.mis.beta.dto.view.PoFinalReport;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.EditStatus;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;

import lombok.NonNull;

/**
 * Spring repository for accessing all notification information and requirements of production processes.
 * 
 * @author Zvi
 *
 */
public interface ProcessInfoRepository extends ProcessRepository<PoProcess> {
	
	@Query("select a "
			+ "from ProcessManagement a "
			+ "join fetch a.user "
			+ "join GeneralProcess p "
				+ "on p.processType = a.processType "
			+ "where p.id = ?1")
	List<ProcessManagement> findProcessTypeAlertsByProcess(Integer processId);

	@Query("select user "
			+ "from ProcessManagement a "
			+ "join a.user user "
			+ "join GeneralProcess p "
				+ "on p.processType = a.processType "
			+ "where p.id = ?1")
	Set<UserEntity> findProcessTypeAlertsUsersByProcess(Integer processId);
	
	@Query("select p.approvals from GeneralProcess p where p.id = ?1")
	List<ApprovalTask> findProcessApprovals(Integer processId);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
				+ "m.id, m.version, c.id, t.code, t.suffix, s.name, m.description, p.id, pt.processName, pt.value, m.createdDate, pr.name, prm.name,  m.label) "
			+ "from UserMessage m "
				+ "left join m.process p "
					+ "left join p.processType pt "
					+ "left join p.poCode c "
						+ "left join c.contractType t "
						+ "left join c.supplier s "
				+ "join m.user u "
					+ "join u.person pr "
				+ "join m.modifiedBy um "
					+ "join um.person prm "
			+ "where u.id = ?1 "
			+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessagesByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, c.id, t.code, t.suffix, s.name, pa.description, p.id, pt.processName, pt.value, pa.createdDate, "
			+ "pr.name, prm.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
			+ "join pa.process p "
				+ "join p.processType pt "
				+ "left join p.poCode c "
					+ "left join c.contractType t "
					+ "left join c.supplier s "
			+ "join pa.user u "
				+ "join u.person pr "
			+ "join pa.modifiedBy um "
				+ "join um.person prm "
		+ "where pa.decision in :decisions "
			+ "and u.id = :userId "
		+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findAllRequiredApprovalsByUser(Integer userId, DecisionType[] decisions);

	@Query("select new com.avc.mis.beta.dto.data.ApprovalTaskDTO("
			+ "pa.id, pa.version, c.id, t.code, t.suffix, s.name, pa.description, p.id, pt.processName, pt.value, pa.createdDate, "
			+ "pr.name, prm.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
		+ "join pa.process p "
			+ "join p.processType pt "
		+ "left join p.poCode c "
			+ "left join c.contractType t "
			+ "left join c.supplier s "
		+ "join pa.user u "
		+ "join u.person pr "
		+ "join pa.modifiedBy um "
		+ "join um.person prm "
		+ "where u.id = :userId "
		+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findAllApprovalsByUser(Integer userId);

	@Query("select new com.avc.mis.beta.dto.data.UserMessageDTO("
			+ "m.id, m.version, c.id, t.code, t.suffix, s.name, m.description, p.id, pt.processName, pt.value, m.createdDate, pr.name, prm.name, m.label) "
		+ "from UserMessage m "
			+ "join m.process p "
				+ "join p.processType pt "
			+ "left join p.poCode c "
				+ "left join c.contractType t "
				+ "left join c.supplier s "
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
			+ "where a.id = :id")
	ProcessManagement findProcessManagementById(Integer id);

	@Query("select new com.avc.mis.beta.dto.data.ProcessManagementDTO(a.id, t.processName, "
				+ "u.id, u.version, u.username, a.managementType) "
			+ "from ProcessManagement a "
				+ "join a.user u "
				+ "join a.processType t ")
	List<ProcessManagementDTO> findAllProcessManagements();

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


	@Query("select c.editStatus "
			+ "from ProcessLifeCycle c "
				+ "join c.process p "
			+ "where p.id = :processId ")
	EditStatus findProcessEditStatus(Integer processId);

	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from TransactionProcess p "
				+ "join p.usedItemGroups grp "
					+ "join grp.usedItems ui "
						+ "join ui.storage s "
							+ "join s.processItem pi "
								+ "join pi.process ui_origion_p "
									+ "join ui_origion_p.lifeCycle c "
			+ "where ui_origion_p.id = :processId and "
				+ "c.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED")
	Boolean isProcessReferenced(Integer processId);


	@Query("select a.managementType "
		+ "from ProcessManagement a "
			+ "join a.user u "
			+ "join a.processType t "
		+ "where t.processName = :processName "
			+ "and u.id = :currentUserId ")
	List<ManagementType> findUserProcessPrivilige(ProcessName processName, Integer currentUserId);


	@Query("select a "
		+ "from ApprovalTask a "
			+ "join a.process p "
			+ "join a.user u "
		+ "where p.id = :processId "
			+ "and u.id = :currentUserId ")
	Optional<ApprovalTask> findProcessApprovalByProcessAndUser(int processId, Integer currentUserId);


	@Query("select new com.avc.mis.beta.dto.data.ProcessManagementDTO(a.id, t.processName, "
			+ "u.id, u.version, u.username, a.managementType) "
		+ "from ProcessManagement a "
			+ "join a.user u "
			+ "join a.processType t "
		+ "where u.id = :currentUserId ")
	List<ProcessManagementDTO> findAllUserProcessPrivilige(Integer currentUserId);

	@Query("select new com.avc.mis.beta.dto.view.PoFinalReport("
			+ "po_code.code, c.code, c.suffix, s.name) "
		+ "from PoCode po_code "
				+ "join po_code.contractType c "
				+ "join po_code.supplier s "
		+ "where po_code.code = :poCodeId ")
	PoFinalReport findFinalReportBasic(@NonNull Integer poCodeId);

	

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
	

//	@Query("select a "
//			+ "from ProcessManagement a "
//			+ "join fetch a.user "
//			+ "where a.processType = ?1")
//	List<ProcessManagement> findProcessTypeAlerts(ProcessType processType);

}
