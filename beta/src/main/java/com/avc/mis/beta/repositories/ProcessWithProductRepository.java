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
			+ "SUM((sf.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor) "
			+ ") "
		+ "from PoProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
					+ "join item.unit item_unit "
				+ "join pi.storageForms sf "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
		+ "where "
			+ "p.id in :processIds "
		+ "group by item.id ")
	Stream<ItemAmount> findSummaryProducedItemAmounts(int[] processIds);


}
