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
			+ "SUM((ui.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.poCode po_code "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
							+ "join pi.process p_used_item "
								+ "join p_used_item.poCode po_code_used_item "
//						+ "join sf.group sf_group "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
						+ "left join sf.warehouseLocation wh "
//			+ "join p.processType pt "
		+ "where "
//			+ "pt.processName = :processName "
			+ "p.id in :processIds "
			+ "and po_code_used_item.code = po_code.code "
		+ "group by p, item "
		+ "order by grp.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((sf.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
					+ "join item.unit item_unit "
				+ "join pi.storageForms sf "
//					+ "join sf.group sf_group "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
					+ "left join sf.warehouseLocation wh "
//			+ "join p.processType pt "
		+ "where "
//			+ "pt.processName = :processName "
			+ "p.id in :processIds "
		+ "group by p, item "
		+ "order by pi.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllProducedItemsByProcessIds(int[] processIds);
	
	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
//			+ "p.id, "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((ui.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor)) "
//			+ "function('GROUP_CONCAT', wh.value)) "
		+ "from TransactionProcess p "
			+ "join p.poCode po_code "
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
//						+ "left join sf.warehouseLocation wh "
//			+ "join p.processType pt "
		+ "where "
//			+ "pt.processName = :processName "
			+ "p.id in :processIds "
//			+ "and po_code_used_item.code = po_code.code "
		+ "group by item.id ")
//		+ "order by grp.ordinal ")
	Stream<ItemAmount> findSummaryUsedItemAmounts(int[] processIds);

	
//	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
////			+ "p.id, "
//			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
//			+ "item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "SUM((sf.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor) "
////			+ "function('GROUP_CONCAT', wh.value)"
//			+ ") "
//		+ "from TransactionProcess p "
//			+ "join p.processItems pi "
//				+ "join pi.item item "
//					+ "join item.unit item_unit "
//				+ "join pi.storageForms sf "
////					+ "join sf.group sf_group "
//						+ "join UOM uom "
//							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
////					+ "left join sf.warehouseLocation wh "
////			+ "join p.processType pt "
//		+ "where "
////			+ "pt.processName = :processName "
//			+ "p.id in :processIds "
//		+ "group by item.id ")
//	Stream<ItemAmount> findSummaryProducedItemAmounts(int[] processIds);


}
