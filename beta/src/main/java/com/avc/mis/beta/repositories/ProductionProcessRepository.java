/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author zvi
 *
 */
public interface ProductionProcessRepository extends ProcessRepository<ProductionProcess> {

	@Query("select new com.avc.mis.beta.dto.process.ProductionProcessDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from ProductionProcess r "
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
	Optional<ProductionProcessDTO> findProductionProcessDTOById(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((ui.numberUnits * (unit.amount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', wh)) "
		+ "from TransactionProcess p "
			+ "join p.poCode po_code "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
							+ "join pi.process p_used_item "
								+ "join p_used_item.poCode po_code_used_item "
						+ "join sf.unitAmount unit "
						+ "join UOM uom "
							+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
						+ "left join sf.warehouseLocation wh "
			+ "join p.processType pt "
		+ "where pt.processName = :processName "
			+ "and po_code_used_item.code = po_code.code "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByProcessType(ProcessName processName);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((sf.numberUnits * (unit.amount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', wh)) "
		+ "from TransactionProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
				+ "join pi.storageForms sf "
					+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
					+ "left join sf.warehouseLocation wh "
			+ "join p.processType pt "
		+ "where pt.processName = :processName "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllProducedItemsByProcessType(ProcessName processName);

	
}
