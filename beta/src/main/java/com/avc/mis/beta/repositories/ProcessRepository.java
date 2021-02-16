package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.basic.ProcessBasic;
import com.avc.mis.beta.dto.embedable.GeneralProcessInfo;
import com.avc.mis.beta.dto.embedable.PoProcessInfo;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.processinfo.WeightedPoDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.query.UsedItemWithGroup;
import com.avc.mis.beta.dto.view.ProcessRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.GeneralProcess;

import lombok.NonNull;

/**
 * @author Zvi
 *
 */
public interface ProcessRepository<T extends GeneralProcess> extends BaseRepository<T> {
	
	@Query("select new com.avc.mis.beta.dto.embedable.GeneralProcessInfo("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from PoProcess r "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
		+ "where type(r) = :clazz "
			+ "and r.id = :processId "
		+ "group by r ")
	Optional<GeneralProcessInfo> findGeneralProcessInfoByProcessId(int processId, Class<? extends T> clazz);

	@Query("select new com.avc.mis.beta.dto.view.ProcessRow("
			+ "p.id, po_code.id, po_code.code, t.code, t.suffix, s.name, po_code.display, "
			+ "CASE "
				+ "WHEN po_code is null "
					+ "THEN function('GROUP_CONCAT', concat(w_t.code, '-', w_po_code.code, w_t.suffix)) "
				+ "ELSE function('GROUP_CONCAT', concat(t.code, '-', po_code.code, t.suffix)) "
			+ "END, "
			+ "p.recordedTime, p.duration, lc.processStatus, "
			+ "function('GROUP_CONCAT', concat(u.username, ':', approval.decision)) ) "
		+ "from PoProcess p "
			+ "left join p.poCode po_code "
				+ "left join po_code.contractType t "
				+ "left join po_code.supplier s "
			+ "left join p.weightedPos w_po "
				+ "left join w_po.poCode w_po_code "
					+ "left join w_po_code.contractType w_t "
					+ "left join w_po_code.supplier w_s "
			+ "join p.processType pt "
			+ "join p.lifeCycle lc "
			+ "left join p.approvals approval "
				+ "left join approval.user u "
		+ "where pt.processName = :processName "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
		+ "group by p "
		+ "order by p.recordedTime desc ")
	List<ProcessRow> findProcessByType(ProcessName processName, Integer poCodeId, boolean cancelled);

	
		
	@Query("select new com.avc.mis.beta.dto.query.UsedItemWithGroup( "
			+ "grp.id, grp.version, grp.ordinal, grp.groupName, grp.tableView, "
			+ "i.id, i.version, i.ordinal, i.numberUnits, "
			+ "item.id, item.value, item.measureUnit, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "sf_group.measureUnit, used_p.recordedTime, "
			+ "itemPo.id, itemPo.code, ct.code, ct.suffix, s.name, itemPo.display, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (sf_ui <> i AND sf_used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN sf_ui.numberUnits "
					+ "ELSE 0 "
				+ "END)"
			+ "), "
			+ "sf.accessWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks) "
		+ "from UsedItem i "
			+ "join i.storage sf "
				+ "join sf.group sf_group "
				+ "left join sf.warehouseLocation warehouseLocation "
				+ "join sf.processItem pi "
					+ "join pi.item item "
						+ "join item.unit item_unit "
					+ "join pi.process used_p "
						+ "join used_p.poCode itemPo "
							+ "left join itemPo.contractType ct "
							+ "left join itemPo.supplier s "
				+ "join sf.usedItems sf_ui "
					+ "join sf_ui.group sf_used_g "
						+ "join sf_used_g.process sf_used_p "
							+ "join sf_used_p.lifeCycle sf_used_lc "
			+ "join i.group grp "
				+ "join grp.process p "
		+ "where p.id = :processId "
		+ "group by sf "
		+ "order by grp.ordinal, i.ordinal ")
	List<UsedItemWithGroup> findUsedItemsWithGroup(int processId);
	

	/**
	 * Gets the join of process item, process and storage information for the given process.
	 * @param processId id of the process
	 * @return List of ProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
			+ " i.id, i.version, i.ordinal, "
			+ "item.id, item.value, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), sf_group.measureUnit, "
			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, poCode.display, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, sf.accessWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.groupName, i.description, i.remarks, i.tableView) "
		+ "from ProcessItem i "
			+ "join i.item item "
				+ "join item.unit item_unit "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.group sf_group "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId "
		+ "order by i.ordinal , sf.ordinal, sf.dtype "
//		+ ", sf.ordinal " //already sorted in dto setter for comparing between classes as well
		+ "")
	List<ProcessItemWithStorage> findProcessItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.WeightedPoDTO( "
			+ "weighted_po.id, weighted_po.version, weighted_po.ordinal, "
			+ "po_code.id, po_code.code, ct.code, ct.suffix, s.name, po_code.display, "
			+ "weighted_po.weight) "
		+ "from PoProcess p "
			+ "join p.weightedPos weighted_po "
				+ "join weighted_po.poCode po_code "
					+ "join po_code.contractType ct "
					+ "join po_code.supplier s "
		+ "where p.id = :processId "
		+ "order by weighted_po.ordinal ")
	List<WeightedPoDTO> findWeightedPos(Integer processId);


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
		+ "where c.id = :poCodeId ")
	List<ProcessBasic> findAllProcessesByPo(Integer poCodeId);
	
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
	List<ProcessBasic> findAllProcessesByPoAndName(@NonNull Integer poCodeId, Set<ProcessName> processNames);


	@Query("select new com.avc.mis.beta.dto.basic.ProcessBasic( "
			+ "p.id, t.processName, type(p)) "
		+ "from GeneralProcess p "
			+ "join p.processType t "
		+ "where p.id = :processId ")
	ProcessBasic findProcessBasic(@NonNull Integer processId);

//	@Query("select new com.avc.mis.beta.dto.process.inventory.UsedItemDTO( "
//			+ "i.id, i.version, i.ordinal, i.numberUnits, "
//			+ "item.id, item.value, item.measureUnit, "
//			+ "item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "sf_group.measureUnit, used_p.recordedTime, "
//			+ "itemPo.id, itemPo.code, ct.code, ct.suffix, s.name, itemPo.display, "
//			+ "sf.id, sf.version, sf.ordinal, "
//			+ "sf.unitAmount, sf.numberUnits, "
//			+ "SUM("
//				+ "(CASE "
//					+ "WHEN (sf_ui <> i AND sf_used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
//						+ "THEN sf_ui.numberUnits "
//					+ "ELSE 0 "
//				+ "END)"
//			+ "), "
//			+ "sf.accessWeight, "
//			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks) "
//		+ "from UsedItem i "
//			+ "join i.storage sf "
//				+ "join sf.group sf_group "
//				+ "left join sf.warehouseLocation warehouseLocation "
//				+ "join sf.processItem pi "
//					+ "join pi.item item "
//						+ "join item.unit item_unit "
//					+ "join pi.process used_p "
//						+ "left join used_p.poCode itemPo "
//							+ "left join itemPo.contractType ct "
//							+ "left join itemPo.supplier s "
//				+ "join sf.usedItems sf_ui "
//					+ "join sf_ui.group sf_used_g "
//						+ "join sf_used_g.process sf_used_p "
//							+ "join sf_used_p.lifeCycle sf_used_lc "
//			+ "join i.group grp "
//				+ "join grp.process p "
//		+ "where p.id = :processId "
//		+ "group by sf "
//		+ "order by i.ordinal ")
//	List<UsedItemDTO> findUsedItems(int processId);

}
