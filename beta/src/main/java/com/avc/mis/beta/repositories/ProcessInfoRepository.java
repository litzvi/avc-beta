/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ProcessManagementDTO;
import com.avc.mis.beta.dto.processinfo.ApprovalTaskDTO;
import com.avc.mis.beta.dto.processinfo.UserMessageDTO;
import com.avc.mis.beta.dto.query.UsedItemAmountWithPoCode;
import com.avc.mis.beta.dto.query.UsedProcessWithPoCode;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.codes.BasePoCode;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.processinfo.ApprovalTask;

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
	
	@Query("select p.approvals from GeneralProcess p where p.id = :processId")
	List<ApprovalTask> findProcessApprovals(Integer processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.UserMessageDTO("
			+ "m.id, m.version, "
			+ "function('GROUP_CONCAT', concat(t.code, '-', po_code.code, coalesce(t.suffix, ''))), "
			+ "function('GROUP_CONCAT', s.name), "
			+ "m.description, p.id, pt.processName, pt.value, m.createdDate, prm.name, pr.name, m.label) "
		+ "from UserMessage m "
			+ "left join m.process p "
				+ "left join p.processType pt "
			+ "left join PoProcess po_p "
				+ "on p.id = po_p.id "
			+ "left join po_p.poCode p_po_code "
			+ "left join po_p.weightedPos w_po "
				+ "left join w_po.poCode w_po_code "
				+ "left join BasePoCode po_code "
					+ "on (po_code = p_po_code or po_code = w_po_code) "
					+ "left join po_code.contractType t "
					+ "left join po_code.supplier s "
			+ "join m.modifiedBy um "
				+ "join um.person prm "
			+ "join m.user u "
				+ "join u.person pr "
		+ "where u.id = :userId "
			+ "and (:lables is null or m.label in :lables) "
		+ "group by m "
		+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessages(Integer userId, List<MessageLabel> lables);
	

	@Query("select new com.avc.mis.beta.dto.processinfo.ApprovalTaskDTO("
			+ "pa.id, pa.version, "
			+ "function('GROUP_CONCAT', concat(t.code, '-', po_code.code, coalesce(t.suffix, ''))), "
			+ "function('GROUP_CONCAT', s.name), "
			+ "pa.description, p.id, pt.processName, pt.value, pa.createdDate, "
			+ "prm.name, pr.name, pa.decision, pa.processSnapshot) "
		+ "from ApprovalTask pa "
			+ "join pa.process p "
				+ "join p.processType pt "
			+ "left join PoProcess po_p "
				+ "on p.id = po_p.id "
				+ "left join po_p.poCode p_po_code "
				+ "left join po_p.weightedPos w_po "
					+ "left join w_po.poCode w_po_code "
					+ "left join BasePoCode po_code "
							+ "on (po_code = p_po_code or po_code = w_po_code) "
						+ "left join po_code.contractType t "
						+ "left join po_code.supplier s "
			+ "join pa.modifiedBy um "
				+ "join um.person prm "
			+ "join pa.user u "
				+ "join u.person pr "
			+ "where pa.decision in :decisions "
				+ "and u.id = :userId "
			+ "group by pa "
			+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findApprovals(Integer userId, DecisionType[] decisions);

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


	@Query("select c "
			+ "from ProcessLifeCycle c "
				+ "join c.process p "
			+ "where p.id = :processId ")
	ProcessLifeCycle findProcessEditStatus(Integer processId);

	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from ProcessWithProduct p "
				+ "join p.processItems pi "
					+ "join pi.storageForms s "
						+ "join s.usedItems ui "
							+ "join ui.group used_g "
								+ "join used_g.process used_p "
									+ "join used_p.lifeCycle used_lc "
//			+ "from TransactionProcess p "
//				+ "join p.usedItemGroups grp "
//					+ "join grp.usedItems ui "
//						+ "join ui.storage s "
//							+ "join s.processItem pi "
//								+ "join pi.process ui_origion_p "
//				+ "join p.lifeCycle c "
			+ "where p.id = :processId "
				+ "and used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED ")
	Boolean isProcessReferenced(Integer processId);
	
	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from PO po "
				+ "join po.orderItems oi "
					+ "join oi.receiptItems ri "
						+ "join ri.process r "
							+ "join r.lifeCycle r_lc "		
			+ "where po.id = :processId "
				+ "and r_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED ")
	Boolean isOrderReferenced(Integer processId);



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

//	@Query("select new com.avc.mis.beta.dto.view.PoFinalReport("
//			+ "po_code.id, po_code.code, c.code, c.suffix, s.name, po_code.display) "
//		+ "from PoCode po_code "
//				+ "join po_code.contractType c "
//				+ "join po_code.supplier s "
//		+ "where po_code.id = :poCodeId ")
//	PoFinalReport findFinalReportBasic(@NonNull Integer poCodeId);

	//TODO check if can change number of units
	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from ProcessWithProduct p "
				+ "join p.processItems pi "
					+ "join pi.storageForms sf "
						+ "join sf.usedItems using_items "
							+ "join using_items.group ui_g "
								+ "join ui_g.process ui_origion_p "
									+ "join ui_origion_p.lifeCycle ui_p_lc "
			+ "where p.id = :processId "
				+ "and ui_p_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ "and sf.id not in :storageIds ")
	Boolean isRemovingUsedProduct(Integer processId, Set<Integer> storageIds);
	
	@Query("select new java.lang.Boolean(count(*) > 0) "
			+ "from StorageRelocation p "
				+ "join p.storageMovesGroups grp "
					+ "join grp.storageMoves sf "
						+ "join sf.usedItems using_items "
							+ "join using_items.group ui_g "
								+ "join ui_g.process ui_origion_p "
									+ "join ui_origion_p.lifeCycle ui_p_lc "
			+ "where p.id = :processId "
				+ "and ui_p_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ "and sf.id not in :storageIds ")
	Boolean isRelocationRemovingUsedProduct(Integer processId, Set<Integer> storageIds);

	@Query("select new com.avc.mis.beta.dto.query.UsedItemAmountWithPoCode("
				+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
				+ "used_p.id, used_p.version, used_type.processName, type(used_p), "
				+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
				+ "item_unit.amount, item_unit.measureUnit, type(item), "
				+ "SUM( "
					+ "((ui.numberUnits * sf.unitAmount) * uom.multiplicand / uom.divisor) "
					+ " * coalesce(w_po.weight, 1)"
				+ ") as po_item_amount ) "
			+ "from TransactionProcess p "
				+ "join p.usedItemGroups grp "
					+ "join grp.usedItems ui "
						+ "join ui.storage sf "
							+ "join sf.processItem pi "
								+ "join pi.item item "
									+ "join item.unit item_unit "
								+ "join UOM uom "
									+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
							+ "join sf.group used_grp "
								+ "join used_grp.process used_p "
									+ "join used_p.processType used_type "
									+ "left join used_p.poCode p_po_code "
									+ "left join used_p.weightedPos w_po "
										+ "left join w_po.poCode w_po_code "
									+ "join BasePoCode po_code "
										+ "on (po_code = p_po_code or po_code = w_po_code) "
										+ "join po_code.contractType t "
										+ "join po_code.supplier s "
			+ "where p.id = :processId "
				+ "and item.itemGroup = com.avc.mis.beta.entities.item.ItemGroup.PRODUCT "
			+ "group by used_p, po_code, item "
			+ "order by po_item_amount desc ")
	List<UsedItemAmountWithPoCode> generateWeightedPos(Integer processId);

	@Query("select new com.avc.mis.beta.dto.query.UsedProcessWithPoCode( "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "used_p.id, used_p.version, used_type.processName, type(used_p)) "
		+ "from TransactionProcess p "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.group used_grp "
							+ "join used_grp.process used_p "
								+ "join used_p.processType used_type "
								+ "left join used_p.poCode p_po_code "
								+ "left join used_p.weightedPos w_po "
									+ "left join w_po.poCode w_po_code "
								+ "join BasePoCode po_code "
									+ "on (po_code = p_po_code or po_code = w_po_code) "
									+ "join po_code.contractType t "
									+ "join po_code.supplier s "
		+ "where p.id = :processId "
		+ "group by po_code")
	List<UsedProcessWithPoCode> getUsedProcessWithPoCodes(Integer processId);
	
	@Query("select new com.avc.mis.beta.dto.query.UsedProcessWithPoCode( "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "used_p.id, used_p.version, used_type.processName, type(used_p)) "
		+ "from StorageRelocation p "
			+ "join p.storageMovesGroups grp "
				+ "join grp.storageMoves ui "
					+ "join ui.storage sf "
						+ "join sf.group used_grp "
							+ "join used_grp.process used_p "
								+ "join used_p.processType used_type "
								+ "left join used_p.poCode p_po_code "
								+ "left join used_p.weightedPos w_po "
									+ "left join w_po.poCode w_po_code "
								+ "join BasePoCode po_code "
									+ "on (po_code = p_po_code or po_code = w_po_code) "
									+ "join po_code.contractType t "
									+ "join po_code.supplier s "
		+ "where p.id = :processId "
		+ "group by po_code, used_p ")
	List<UsedProcessWithPoCode> getRelocationUsedProcessWithPoCodes(Integer processId);
	
	@Query("select used_p.id, using_p.id  "
			+ "from WeightedPo w_po "
				+ "join w_po.process using_p "
				+ "join w_po.usedProcess used_p "
				+ "join w_po.poCode w_po_code "
			+ "where w_po_code.id in :poCodeIds "
			+ "group by used_p.id, using_p.id ")
	List<Integer[]> findTransactionProcessVertices(Integer[] poCodeIds);	
		

	

	

	

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
