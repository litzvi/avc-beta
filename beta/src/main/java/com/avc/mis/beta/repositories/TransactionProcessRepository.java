/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.TransactionProcess;

/**
 * @author zvi
 *
 */
public interface TransactionProcessRepository<T extends TransactionProcess<?>> extends PoProcessRepository<T> {
	
	

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((ui.numberUsedUnits * (unit.amount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', wh.value)) "
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
		+ "group by p, item "
//		+ "order by grp.ordinal "
		+ "")
	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByProcessType(ProcessName processName);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((sf.numberUnits * (unit.amount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', wh.value)) "
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
		+ "group by p, item "
//		+ "order by pi.ordinal"
		+ " ")
	Stream<ProductionProcessWithItemAmount> findAllProducedItemsByProcessType(ProcessName processName);

}
