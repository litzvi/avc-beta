/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.data.ProcessManagementDTO;
import com.avc.mis.beta.dto.process.collection.ApprovalTaskDTO;
import com.avc.mis.beta.dto.process.collection.UserMessageDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithPoCode;
import com.avc.mis.beta.dto.query.UsedProcess;
import com.avc.mis.beta.entities.data.ProcessManagement;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.entities.enums.DecisionType;
import com.avc.mis.beta.entities.enums.ManagementType;
import com.avc.mis.beta.entities.enums.MessageLabel;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.ProcessLifeCycle;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;

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

	@Query("select new com.avc.mis.beta.dto.process.collection.UserMessageDTO("
			+ "m.id, m.version, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(t.code, '-', po_code.code, coalesce(t.suffix, '')))), "
			+ "function('GROUP_CONCAT', function('DISTINCT', s.name)), "
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
			+ "and (:startTime is null or m.createdDate >= :startTime) "
			+ "and (:endTime is null or m.createdDate < :endTime) "
		+ "group by m "
		+ "ORDER BY m.createdDate DESC ")
	List<UserMessageDTO> findAllMessages(Integer userId, List<MessageLabel> lables, LocalDateTime startTime, LocalDateTime endTime);
	

	@Query("select new com.avc.mis.beta.dto.process.collection.ApprovalTaskDTO("
			+ "pa.id, pa.version, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(t.code, '-', po_code.code, coalesce(t.suffix, '')))), "
			+ "function('GROUP_CONCAT', function('DISTINCT', s.name)), "
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
				+ "and (:startTime is null or p.modifiedDate >= :startTime) "
				+ "and (:endTime is null or p.modifiedDate < :endTime) "
			+ "group by pa "
			+ "ORDER BY p.modifiedDate DESC ")
	List<ApprovalTaskDTO> findApprovals(Integer userId, DecisionType[] decisions, LocalDateTime startTime, LocalDateTime endTime);
	
	@Query("select count(*) "
		+ "from UserMessage m "
			+ "join m.user u "
		+ "where u.id = :userId ")
	Integer findUserMassagesNumber(Integer userId);
	
	@Query("select count(*) "
		+ "from ApprovalTask pa "
			+ "join pa.user u "
		+ "where u.id = :userId ")
	Integer findUserTasksNumber(Integer userId);

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
			+ "from StorageRelocation p "
				+ "join p.storageMovesGroups grp "
					+ "join grp.storageMoves s "
						+ "join s.usedItems ui "
							+ "join ui.group used_g "
								+ "join used_g.process used_p "
									+ "join used_p.lifeCycle used_lc "
			+ "where p.id = :processId "
				+ "and used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED ")
	Boolean isRelocationReferenced(Integer processId);
	
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
	
	@Query("select new java.lang.Boolean(count(*) = 0) "
			+ "from ProcessParent pp "
				+ "join pp.process p "
					+ "join p.lifeCycle p_lc "
				+ "join pp.usedProcess up "
					+ "join up.lifeCycle up_lc "
			+ "where up.id = :processId "
				+ "and p_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ "and up_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
				+ "and p.recordedTime < up.recordedTime ")
	Boolean isProcessSynchronized(Integer processId);


	@Query("select new com.avc.mis.beta.dto.query.ItemAmountWithPoCode("
				+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
//				+ "used_p.id, used_p.version, used_type.processName, type(used_p), "
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
//							+ "join sf.group used_grp "
//								+ "join used_grp.process used_p "
//									+ "join used_p.processType used_type "
								+ "join pi.process used_p "
									+ "left join used_p.poCode p_po_code "
									+ "left join used_p.weightedPos w_po "
										+ "left join w_po.poCode w_po_code "
									+ "join BasePoCode po_code "
										+ "on (po_code = p_po_code or po_code = w_po_code) "
										+ "join po_code.contractType t "
										+ "join po_code.supplier s "
			+ "where p.id = :processId "
				+ "and item.itemGroup = :itemGroup "
			+ "group by po_code, item "
//			+ "group by used_p, po_code, item "
			+ "order by po_item_amount desc ")
	List<ItemAmountWithPoCode> findTransactionWeightedPos(Integer processId, ItemGroup itemGroup);
	
	@Query("select new com.avc.mis.beta.dto.query.ItemAmountWithPoCode("
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM( "
				+ "((ui.numberUnits * sf.unitAmount) * uom.multiplicand / uom.divisor) "
				+ " * coalesce(w_po.weight, 1)"
			+ ") as po_item_amount ) "
		+ "from StorageRelocation p "
			+ "join p.storageMovesGroups grp "
				+ "join grp.storageMoves ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
							+ "join pi.process used_p "
								+ "left join used_p.poCode p_po_code "
								+ "left join used_p.weightedPos w_po "
									+ "left join w_po.poCode w_po_code "
								+ "join BasePoCode po_code "
									+ "on (po_code = p_po_code or po_code = w_po_code) "
									+ "join po_code.contractType t "
									+ "join po_code.supplier s "
		+ "where p.id = :processId "
//			+ "and item.itemGroup = com.avc.mis.beta.entities.item.ItemGroup.PRODUCT " - in relocation should be for any moved item
		+ "group by po_code, item "
		+ "order by po_item_amount desc ")
	List<ItemAmountWithPoCode> findRelocationWeightedPos(Integer processId);
	
	@Query("select new com.avc.mis.beta.dto.query.UsedProcess( "
			+ "used_p.id, used_p.version, used_type.processName, type(used_p), used_p.recordedTime) "
		+ "from TransactionProcess p "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.group used_grp "
							+ "join used_grp.process used_p "
								+ "join used_p.processType used_type "
		+ "where p.id = :processId "
		+ "group by used_p.id")
	List<UsedProcess> findTransactionUsedProcess(Integer processId);
		
	@Query("select new com.avc.mis.beta.dto.query.UsedProcess( "
			+ "used_p.id, used_p.version, used_type.processName, type(used_p), used_p.recordedTime) "
		+ "from StorageRelocation p "
			+ "join p.storageMovesGroups grp "
				+ "join grp.storageMoves ui "
					+ "join ui.storage sf "
						+ "join sf.group used_grp "
							+ "join used_grp.process used_p "
								+ "join used_p.processType used_type "
		+ "where p.id = :processId "
		+ "group by used_p.id ")
	List<UsedProcess> findRelocationUsedProcess(Integer processId);
	
	@Query("select used_p.id, using_p.id  "
			+ "from ProcessParent pp "
			+ "join WeightedPo w_po "
				+ "on pp.process = w_po.process "
				+ "join pp.process using_p "
				+ "join pp.usedProcess used_p "
				+ "join w_po.poCode w_po_code "
			+ "where w_po_code.id in :poCodeIds "
			+ "group by used_p.id, using_p.id ")
	List<Integer[]> findTransactionProcessVertices(Integer[] poCodeIds);

	

}
