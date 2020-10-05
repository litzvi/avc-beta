/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.StorageRelocationDTO;
import com.avc.mis.beta.dto.processinfo.StorageMoveDTO;
import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.processinfo.Storage;

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
			+ "SUM(used_unit.amount * storageMove.numberUsedUnits * uom_used.multiplicand / uom_used.divisor), "
			+ "SUM(producedUnit.amount * storageMove.numberUnits * uom_produced.multiplicand / uom_produced.divisor), "
			+ "item.measureUnit) "
		+ "from StorageRelocation p "
			+ "join p.storageMoves storageMove "
				+ "join storageMove.processItem pi "
					+ "join pi.item item "
				+ "join storageMove.unitAmount producedUnit "
				+ "join UOM uom_produced "
					+ "on uom_produced.fromUnit = producedUnit.measureUnit and uom_produced.toUnit = item.measureUnit "
				+ "join storageMove.storage used_sf "
						+ "join used_sf.unitAmount used_unit "
						+ "join UOM uom_used "
							+ "on uom_used.fromUnit = used_unit.measureUnit and uom_used.toUnit = item.measureUnit "						
		+ "where p.id = :processId "
		+ "group by pi ")
	List<ProcessItemTransactionDifference> findRelocationDifferences(Integer processId);

	@Query("select s "
		+ "from Storage s "
		+ "where s.id in :storageIds ")
	Stream<Storage> findStoragesById(int[] storageIds);
	
	@Query("select new com.avc.mis.beta.dto.process.StorageRelocationDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
			+ "r.accessWeight) "
		+ "from StorageRelocation r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
		+ "where r.id = :processId ")
	Optional<StorageRelocationDTO> findRelocationDTOByProcessId(int processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.StorageMoveDTO( "
			+ "m.id, m.version, m.ordinal, m.numberUsedUnits, item.id, item.value, "
			+ "itemPo.id, ct.code, ct.suffix, s.name, "
			+ "used_sf.id, used_sf.version, used_sf.ordinal,"
			+ "used_unit.amount, used_unit.measureUnit, used_sf.numberUnits, used_sf.containerWeight, "
			+ "used_warehouseLocation.id, used_warehouseLocation.value, used_sf.remarks, "
			+ "unit.amount, unit.measureUnit, m.numberUnits, m.containerWeight,"
			+ "warehouseLocation.id, warehouseLocation.value, type(m)) "
		+ "from StorageMove m "
			+ "join m.storage used_sf "
				+ "join used_sf.unitAmount used_unit "
				+ "left join used_sf.warehouseLocation used_warehouseLocation "
				+ "join used_sf.processItem pi "
					+ "join pi.item item "
					+ "join pi.process used_p "
						+ "join used_p.poCode itemPo "
							+ "left join itemPo.contractType ct "
							+ "left join itemPo.supplier s "
			+ "join m.unitAmount unit "
			+ "left join m.warehouseLocation warehouseLocation "
			+ "join m.process p "
		+ "where p.id = :processId "
		+ "order by m.ordinal ")
	List<StorageMoveDTO> findStorageMoveDTOsByProcessId(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((m.numberUsedUnits * (unit.amount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', wh.value)) "
		+ "from StorageRelocation p "
			+ "join p.storageMoves m "
				+ "join m.storage sf "
					+ "join sf.processItem pi "
						+ "join pi.item item "
						+ "join sf.unitAmount unit "
						+ "join UOM uom "
							+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
						+ "left join sf.warehouseLocation wh "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllMovedItemsByProcessType();



}
