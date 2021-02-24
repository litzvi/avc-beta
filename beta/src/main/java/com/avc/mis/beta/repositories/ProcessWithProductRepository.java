/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.entities.process.ProcessWithProduct;

/**
 * @author zvi
 *
 */
public interface ProcessWithProductRepository<T extends ProcessWithProduct<?>> extends PoProcessRepository<T> {
	
	


	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM( "
				+ "((sf.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor) "
				+ " * coalesce(w_po.weight, 1)) "
			+ ")"
		+ "from PoProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
					+ "join item.unit item_unit "
				+ "join pi.storageForms sf "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join p.poCode p_po_code "
					+ "left join p_po_code.weightedPos w_po "
						+ "left join w_po.poCode w_po_code "
					+ "join PoCode po_code "
						+ "on (po_code = p_po_code or po_code = w_po_code) "
		+ "where "
			+ "p.id in :processIds "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
		+ "group by item.id ")
	Stream<ItemAmount> findSummaryProducedItemAmounts(int[] processIds, Integer poCodeId);


}
