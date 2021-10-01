package com.avc.mis.beta.repositories;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.process.collection.WeightedPoDTO;
import com.avc.mis.beta.dto.processInfo.GeneralProcessInfo;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.query.UsedItemWithGroup;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.process.GeneralProcess;
import com.avc.mis.beta.entities.process.PoProcess;
import com.avc.mis.beta.entities.process.collection.ProcessParent;
import com.avc.mis.beta.entities.process.collection.WeightedPo;

import lombok.NonNull;

/**
 * @author Zvi
 *
 */
interface ProcessRepository<T extends GeneralProcess> extends BaseRepository<T> {
	
	@Query("select new com.avc.mis.beta.dto.processInfo.GeneralProcessInfo("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "pt.processName, p_line.id, p_line.value, p_line.productionFunctionality, "
			+ "r.recordedTime, r.shift, r.startTime, r.endTime, r.downtime, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ':', approval.decision)))) "
//			+ "function('GROUP_CONCAT', function('DISTINCT', "
//				+ "(CASE "
//					+ "WHEN u is null THEN '' "
//					+ "WHEN approval is null THEN concat(u.username, ':', 'NOT ATTENDED') "
//					+ "ELSE concat(u.username, ':', approval.decision) "
//				+ "END))"
//			+ ")) "
		+ "from GeneralProcess r "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
//			+ "left join ProcessManagement pm "
//				+ "on pm.processType = r.processType "
//					+ "and (approval is null or pm.user = approval.user) "
//					+ "and pm.managementType = com.avc.mis.beta.entities.enums.ManagementType.APPROVAL "
//			+ "left join UserEntity u "
//				+ "on (u = approval.user or u = pm.user) "
		+ "where type(r) = :clazz "
			+ "and r.id = :processId "
		+ "group by r ")
	Optional<GeneralProcessInfo> findGeneralProcessInfoByProcessId(int processId, Class<? extends T> clazz);
	
	@Query("select new com.avc.mis.beta.dto.view.ProcessRow("
			+ "p.id, pt.value, p_line.value, "
			+ "function('GROUP_CONCAT', function('DISTINCT', po_code.id)), "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(t.code, '-', po_code.code, coalesce(t.suffix, '')))), "
			+ "function('GROUP_CONCAT', function('DISTINCT', s.name)), "
			+ "p.recordedTime, "
			+ "p.startTime, p.endTime, p.downtime, p.numOfWorkers, "
			+ "lc.processStatus, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ': ', approval.decision))), p.remarks) "
		+ "from PoProcess p "
			+ "left join p.poCode p_po_code "
			+ "left join p.weightedPos w_po "
				+ "left join w_po.poCode w_po_code "
				+ "left join BasePoCode po_code "
					+ "on (po_code = p_po_code or po_code = w_po_code) "
					+ "left join po_code.contractType t "
					+ "left join po_code.supplier s "
			+ "join p.processType pt "
			+ "join p.lifeCycle lc "
			+ "left join p.productionLine p_line "
			+ "left join p.approvals approval "
				+ "left join approval.user u "
		+ "where type(p) = :processClass "
			+ "and (pt.processName = :processName or :processName is null) "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
			+ "and (p_line.productionFunctionality = :functionality or :functionality is null) "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
			+ "and (:startTime is null or p.recordedTime >= :startTime) "
			+ "and (:endTime is null or p.recordedTime < :endTime) "
		+ "group by p "
		+ "order by p.recordedTime desc ")
	<S extends PoProcess> List<ProcessRow> findProcessByType(@NonNull Class<S> processClass, ProcessName processName, Integer poCodeId, 
			ProductionFunctionality functionality, boolean cancelled,
			LocalDateTime startTime, LocalDateTime endTime);
		
	@Query("select new com.avc.mis.beta.dto.query.UsedItemWithGroup( "
			+ "g.id, g.version, g.ordinal, g.groupName, g.tableView, "
			+ "i.id, i.version, i.ordinal, i.numberUnits, "
			+ "item.id, item.value, item.measureUnit, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "pi.measureUnit, used_p.recordedTime, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(t.code, '-', po_code.code, coalesce(t.suffix, '')))), "
			+ "function('GROUP_CONCAT', function('DISTINCT', s.name)), "
			+ "grade.id, grade.value,  "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (sf_ui <> i AND sf_used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN "
							+ "CASE WHEN w_po IS NULL THEN (sf_ui.numberUnits) ELSE (sf_ui.numberUnits/size(w_po)) END "
					+ "ELSE 0 "
				+ "END) "
			+ "), "
//			+ "sf.accessWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks) "
		+ "from UsedItem i "
			+ "join i.storage sf "
//				+ "join sf.group sf_group "
				+ "left join sf.warehouseLocation warehouseLocation "
				+ "join sf.processItem pi "
					+ "join pi.item item "
						+ "join item.unit item_unit "
					+ "left join CashewItem cashew_item "
						+ "on item = cashew_item "
						+ "left join cashew_item.grade grade "
					+ "join pi.process used_p "
						+ "left join used_p.poCode p_po_code "
						+ "left join used_p.weightedPos w_po "
							+ "left join w_po.poCode w_po_code "
						+ "left join BasePoCode po_code "
							+ "on (po_code = p_po_code or po_code = w_po_code) "
							+ "left join po_code.contractType t "
							+ "left join po_code.supplier s "
				+ "join sf.usedItems sf_ui "
					+ "join sf_ui.group sf_used_g "
						+ "join sf_used_g.process sf_used_p "
							+ "join sf_used_p.lifeCycle sf_used_lc "
			+ "join i.group g "
				+ "join g.process p "
		+ "where p.id = :processId "
		+ "group by i "
		+ "order by g.ordinal, i.ordinal ")
	List<UsedItemWithGroup> findUsedItemsWithGroup(int processId);
	

	/**
	 * Gets the join of process item, process and storage information for the given process.
	 * @param processId id of the process
	 * @return List of ProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
			+ " i.id, i.version, i.ordinal, "
			+ "item.id, item.value, item.productionUse, "
			+ "item_unit, type(item), sf_group.measureUnit, "
//			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, poCode.display, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, "
//			+ "sf.accessWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.groupName, i.description, i.remarks, i.tableView) "
		+ "from ProcessItem i "
			+ "join i.item item "
				+ "join item.unit item_unit "
			+ "join i.process p "
//				+ "join p.poCode poCode "
//					+ "join poCode.contractType ct "
//					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.group sf_group "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId "
		+ "order by i.ordinal , sf.ordinal, sf.dtype "
//		+ ", sf.ordinal " //already sorted in dto setter for comparing between classes as well
		+ "")
	List<ProcessItemWithStorage> findProcessItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.process.collection.WeightedPoDTO( "
			+ "weighted_po.id, weighted_po.version, weighted_po.ordinal, "
			+ "po_code.id, po_code.code, ct.code, ct.suffix, s.name,  "
			+ "weighted_po.weight) "
		+ "from PoProcess p "
			+ "join p.weightedPos weighted_po "
				+ "join weighted_po.poCode po_code "
					+ "join po_code.contractType ct "
					+ "join po_code.supplier s "
		+ "where p.id = :processId "
		+ "order by weighted_po.ordinal ")
	List<WeightedPoDTO> findWeightedPos(Integer processId);

	@Query("select weighted_po "
		+ "from PoProcess p "
			+ "join p.weightedPos weighted_po "
		+ "where p.id = :processId")
	List<WeightedPo> findWeightedPoReferences(Integer processId);

	@Query("select process_parent "
			+ "from PoProcess p "
				+ "join p.processParents process_parent "
			+ "where p.id = :processId")
	List<ProcessParent> findProcessParentReferences(Integer processId);	

	/**
	 * Gets all processes done for given PoCode
	 * @param poCodeId id of PoCode
	 * @return List of ProcessBasic
	 */
	@Query("select new com.avc.mis.beta.dto.basic.ProcessBasic( "
			+ "p.id, t.processName, type(p)) "
		+ "from PoProcess p "
			+ "join p.processType t "
			+ "left join p.poCode po_code "
			+ "left join p.weightedPos w_po "
				+ "left join w_po.poCode w_po_code "
		+ "where po_code.id = :poCodeId "
			+ "or w_po_code.id = :poCodeId "
		+ "group by p ")
	List<ProcessBasic<GeneralProcess>> findAllProcessesByPo(@NonNull Integer poCodeId);

	
	/**
	 * Gets all processes done for given PoCode
	 * @param poCodeId id of PoCode
	 * @return List of ProcessBasic
	 */
	@Query("select new com.avc.mis.beta.dto.basic.ProcessBasic( "
			+ "p.id, t.processName, type(p)) "
		+ "from PoCode c "
			+ "join c.processes p "
				+ "join p.processType t "
		+ "where c.id = :poCodeId "
			+ "and t.processName in :processNames ")
	List<ProcessBasic<PoProcess>> findAllProcessesByPoAndName(@NonNull Integer poCodeId, Set<ProcessName> processNames);
	
	@Query("select new com.avc.mis.beta.dto.basic.ProcessBasic( "
			+ "p.id, t.processName, type(p)) "
		+ "from PoProcess p "
			+ "join p.processType t ")
	List<ProcessBasic<PoProcess>> findAllProcessesByPoAndName();


	@Query("select new com.avc.mis.beta.dto.basic.ProcessBasic( "
			+ "p.id, t.processName, type(p)) "
		+ "from GeneralProcess p "
			+ "join p.processType t "
		+ "where p.id = :processId ")
	ProcessBasic<GeneralProcess> findProcessBasic(@NonNull Integer processId);

}
