/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.TransactionProcess;

/**
 * @author zvi
 *
 */
public interface TransactionProcessRepository<T extends TransactionProcess<?>> extends PoProcessRepository<T> {
	
	

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((ui.numberUsedUnits * (sf.unitAmount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.defaultMeasureUnit, function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.poCode po_code "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
							+ "join pi.process p_used_item "
								+ "join p_used_item.poCode po_code_used_item "
						+ "join sf.group sf_group "
						+ "join UOM uom "
							+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = item.defaultMeasureUnit "
						+ "left join sf.warehouseLocation wh "
			+ "join p.processType pt "
		+ "where "
//			+ "pt.processName = :processName "
			+ "p.id in :processIds "
			+ "and po_code_used_item.code = po_code.code "
		+ "group by p, item "
		+ "order by grp.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((sf.numberUnits * (sf.unitAmount - coalesce(sf.containerWeight, 0))) * uom.multiplicand / uom.divisor), "
			+ "item.defaultMeasureUnit, function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
				+ "join pi.storageForms sf "
					+ "join sf.group sf_group "
						+ "join UOM uom "
							+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = item.defaultMeasureUnit "
					+ "left join sf.warehouseLocation wh "
			+ "join p.processType pt "
		+ "where "
//			+ "pt.processName = :processName "
			+ "p.id in :processIds "
		+ "group by p, item "
		+ "order by pi.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllProducedItemsByProcessIds(int[] processIds);

}
