/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.TransactionProcess;

/**
 * @author zvi
 *
 */
public interface TransactionProcessRepository<T extends TransactionProcess<?>> extends ProcessWithProductRepository<T> {
	
	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((ui.numberUnits * sf.unitAmount) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
//							+ "join pi.process p_used_item "
//								+ "join p_used_item.poCode po_code_used_item "
//						+ "join sf.group sf_group "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
						+ "left join sf.warehouseLocation wh "
		+ "where "
			+ "p.id in :processIds "
//			+ "and po_code_used_item.id = po_code.id "
		+ "group by p, item "
		+ "order by grp.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((sf.numberUnits * sf.unitAmount) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
					+ "join item.unit item_unit "
				+ "join pi.storageForms sf "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
					+ "left join sf.warehouseLocation wh "
		+ "where "
			+ "p.id in :processIds "
//			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
		+ "group by p, item "
		+ "order by pi.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllProducedItemsByProcessIds(int[] processIds);
	
	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM("
				+ "((ui.numberUnits * sf.unitAmount) * uom.multiplicand / uom.divisor) "
				+ " * coalesce(w_po.weight, 1))"
			+ ") "
		+ "from TransactionProcess p "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
							+ ""
							+ "join pi.process u_p "
								+ "left join u_p.poCode p_po_code "
								+ "left join u_p.weightedPos w_po "
									+ "left join w_po.poCode w_po_code "
									+ "join BasePoCode po_code "
										+ "on (po_code = p_po_code or po_code = w_po_code) "
		+ "where "
			+ "p.id in :processIds "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
		+ "group by item.id ")
	Stream<ItemAmount> findSummaryUsedItemAmounts(int[] processIds, Integer poCodeId);

}
