/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.dto.query.StorageMoveWithGroup;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.process.inventory.StorageBase;

/**
 * @author zvi
 *
 */
public interface RelocationRepository extends PoProcessRepository<StorageRelocation>{

	/**
	 * @param processId
	 * @return
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemTransactionDifference("
			+ "pi.id, "
			+ "SUM(used_sf.unitAmount * storageMove.numberUnits * uom_used.multiplicand / uom_used.divisor), "
			+ "SUM(storageMove.unitAmount * storageMove.numberUnits * uom_produced.multiplicand / uom_produced.divisor), "
			+ "item.measureUnit) "
		+ "from StorageRelocation p "
			+ "join p.storageMovesGroups g "
				+ "join g.storageMoves storageMove "
					+ "join storageMove.processItem pi "
						+ "join pi.item item "
//					+ "join storageMove.unitAmount producedUnit "
//					+ "join storageMove.group produce_group "
						+ "join UOM uom_produced "
							+ "on uom_produced.fromUnit = g.measureUnit and uom_produced.toUnit = item.measureUnit "
					+ "join storageMove.storage used_sf "
						+ "join used_sf.group used_group "
//							+ "join used_sf.unitAmount used_unit "
							+ "join UOM uom_used "
								+ "on uom_used.fromUnit = used_group.measureUnit and uom_used.toUnit = item.measureUnit "						
		+ "where p.id = :processId "
		+ "group by pi ")
	List<ProcessItemTransactionDifference> findRelocationDifferences(Integer processId);

	@Query("select s "
		+ "from StorageBase s "
		+ "where s.id in :storageIds ")
	Stream<StorageBase> findStoragesById(int[] storageIds);
	
//	@Query("select new com.avc.mis.beta.dto.process.StorageRelocationDTO("
//			+ "r.id, r.version, r.createdDate, p_user.username, "
//			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name, po_code.display, "
//			+ "pt.processName, p_line, "
//			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
//			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)) ) "
//		+ "from StorageRelocation r "
//			+ "join r.poCode po_code "
//				+ "join po_code.contractType t "
//				+ "join po_code.supplier s "
//			+ "join r.processType pt "
//			+ "left join r.createdBy p_user "
//			+ "left join r.productionLine p_line "
//			+ "join r.lifeCycle lc "
//			+ "left join r.approvals approval "
//				+ "left join approval.user u "
//		+ "where r.id = :processId "
//		+ "group by r ")
//	Optional<StorageRelocationDTO> findRelocationDTOByProcessId(int processId);

//	@Query("select new com.avc.mis.beta.dto.processinfo.StorageMoveDTO( "
//			+ "m.id, m.version, m.ordinal, m.numberUsedUnits, "
//			+ "item.id, item.value, used_p.recordedTime, "
//			+ "itemPo.id, ct.code, ct.suffix, s.name, "
//			+ "used_sf.id, used_sf.version, used_sf.ordinal,"
//			+ "used_unit.amount, used_unit.measureUnit, used_sf.numberUnits, used_sf.containerWeight, "
//			+ "used_warehouseLocation.id, used_warehouseLocation.value, used_sf.remarks, "
//			+ "unit.amount, unit.measureUnit, m.numberUnits, m.containerWeight,"
//			+ "warehouseLocation.id, warehouseLocation.value, type(m)) "
//		+ "from StorageMove m "
//			+ "join m.storage used_sf "
//				+ "join used_sf.unitAmount used_unit "
//				+ "left join used_sf.warehouseLocation used_warehouseLocation "
//				+ "join used_sf.processItem pi "
//					+ "join pi.item item "
//					+ "join pi.process used_p "
//						+ "join used_p.poCode itemPo "
//							+ "left join itemPo.contractType ct "
//							+ "left join itemPo.supplier s "
//			+ "join m.unitAmount unit "
//			+ "left join m.warehouseLocation warehouseLocation "
//			+ "join m.process p "
//		+ "where p.id = :processId "
//		+ "order by m.ordinal ")
//	List<StorageMoveDTO> findStorageMoveDTOsByProcessId(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((m.numberUnits * sf.unitAmount) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', wh.value)) "
		+ "from StorageRelocation p "
			+ "join p.storageMovesGroups g "
				+ "join g.storageMoves m "
					+ "join m.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
							+ "left join sf.warehouseLocation wh "
		+ "where p.id in :processIds "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllMovedItemsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.query.StorageMoveWithGroup( "
			+ "g.id, g.version, g.ordinal, g.groupName, g.tableView, "
			+ "m.id, m.version, m.ordinal, m.numberUnits, "
			+ "item.id, item.value, item.measureUnit, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "pi.measureUnit, used_p.recordedTime, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "function('GROUP_CONCAT', concat(t.code, '-', po_code.code, coalesce(t.suffix, ''))), "
			+ "function('GROUP_CONCAT', s.name), "
			+ "used_sf.id, used_sf.version, used_sf.ordinal,"
			+ "used_sf.unitAmount, used_sf.numberUnits, "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (sf_ui <> m AND sf_used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN (sf_ui.numberUnits / coalesce(size(used_p.weightedPos), 1)) "
					+ "ELSE 0 "
				+ "END) "
			+ "), "
//			+ "used_sf.accessWeight, "
			+ "used_warehouseLocation.id, used_warehouseLocation.value, used_sf.remarks, "
			+ "m.unitAmount, m.numberUnits, "
//			+ "m.accessWeight,"
			+ "warehouseLocation.id, warehouseLocation.value, type(m)) "
		+ "from StorageMove m "
			+ "join m.storage used_sf "
				+ "left join used_sf.warehouseLocation used_warehouseLocation "
				+ "join used_sf.processItem pi "
					+ "join pi.item item "
						+ "join item.unit item_unit "
					+ "join pi.process used_p "
						+ "left join used_p.poCode p_po_code "
						+ "left join used_p.weightedPos w_po "
							+ "left join w_po.poCode w_po_code "
						+ "left join PoCode po_code "
							+ "on (po_code = p_po_code or po_code = w_po_code) "
							+ "left join po_code.contractType t "
							+ "left join po_code.supplier s "
				+ "join used_sf.usedItems sf_ui "
					+ "join sf_ui.group sf_used_g "
						+ "join sf_used_g.process sf_used_p "
							+ "join sf_used_p.lifeCycle sf_used_lc "
			+ "left join m.warehouseLocation warehouseLocation "
			+ "join m.group g "
				+ "join g.process p "
		+ "where p.id = :processId "
		+ "group by used_sf "
		+ "order by g.ordinal, m.ordinal ")
	List<StorageMoveWithGroup> findStorageMovesWithGroup(int processId);



}
