/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.dto.query.StorageMoveWithGroup;
import com.avc.mis.beta.dto.view.RelocationWithItemAmount;
import com.avc.mis.beta.entities.process.RelocationProcess;
import com.avc.mis.beta.entities.process.storages.StorageBase;

/**
 * @author zvi
 *
 */
public interface RelocationRepository extends PoProcessRepository<RelocationProcess>{

	@Query("select new com.avc.mis.beta.dto.query.ProcessItemTransactionDifference("
			+ "pi.id, "
			+ "SUM(coalesce(used_sf.unitAmount, 1) * storageMove.numberUnits * uom_used.multiplicand / uom_used.divisor), "
			+ "SUM(coalesce(storageMove.unitAmount, 1) * storageMove.numberUnits * uom_produced.multiplicand / uom_produced.divisor), "
			+ "item.measureUnit) "
		+ "from RelocationProcess p "
			+ "join p.storageMovesGroups g "
				+ "join g.storageMoves storageMove "
					+ "join storageMove.processItem pi "
						+ "join pi.item item "
						+ "join UOM uom_produced "
							+ "on uom_produced.fromUnit = g.measureUnit and uom_produced.toUnit = item.measureUnit "
					+ "join storageMove.storage used_sf "
						+ "join used_sf.group used_group "
							+ "join UOM uom_used "
								+ "on uom_used.fromUnit = used_group.measureUnit and uom_used.toUnit = item.measureUnit "						
		+ "where p.id = :processId "
		+ "group by pi ")
	List<ProcessItemTransactionDifference> findRelocationDifferences(Integer processId);

	@Query("select s "
		+ "from StorageBase s "
		+ "where s.id in :storageIds ")
	Stream<StorageBase> findStoragesById(int[] storageIds);

	@Query("select new com.avc.mis.beta.dto.view.RelocationWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((m.numberUnits * coalesce(sf.unitAmount, 1)) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', function('DISTINCT', wh.value)), "
			+ "function('GROUP_CONCAT', function('DISTINCT', new_wh.value))) "
		+ "from RelocationProcess p "
			+ "join p.storageMovesGroups g "
				+ "join g.storageMoves m "
					+ "left join m.warehouseLocation new_wh "
					+ "join m.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
							+ "left join sf.warehouseLocation wh "
		+ "where p.id in :processIds "
		+ "group by p, item ")
	Stream<RelocationWithItemAmount> findAllMovedItemsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.query.StorageMoveWithGroup( "
			+ "g.id, g.version, g.ordinal, g.groupName, g.tableView, "
			+ "m.id, m.version, m.ordinal, m.numberUnits, "
			+ "item.id, item.value, item.measureUnit, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "pi.measureUnit, used_p.recordedTime, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(t.code, '-', po_code.code, coalesce(t.suffix, '')))), "
			+ "function('GROUP_CONCAT', function('DISTINCT', s.name)), "
			+ "grade.id, grade.value, "
			+ "used_sf.id, used_sf.version, used_sf.ordinal,"
			+ "used_sf.unitAmount, used_sf.numberUnits, "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (sf_ui <> m AND sf_used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN (coalesce(sf_ui.numberUnits / size(used_p.weightedPos), sf_ui.numberUnits)) "
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
				+ "join used_sf.usedItems sf_ui "
					+ "join sf_ui.group sf_used_g "
						+ "join sf_used_g.process sf_used_p "
							+ "join sf_used_p.lifeCycle sf_used_lc "
			+ "left join m.warehouseLocation warehouseLocation "
			+ "join m.group g "
				+ "join g.process p "
		+ "where p.id = :processId "
		+ "group by m, used_sf "
		+ "order by g.ordinal, m.ordinal ")
	List<StorageMoveWithGroup> findStorageMovesWithGroup(int processId);



}
