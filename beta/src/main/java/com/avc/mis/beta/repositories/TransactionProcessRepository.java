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
public interface TransactionProcessRepository<T extends TransactionProcess<?>> extends ProcessWithProductRepository<T> {
	
	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((ui.numberUnits * coalesce(sf.unitAmount, 1)) * uom.multiplicand / uom.divisor), "
			+ "function('GROUP_CONCAT', function('DISTINCT', wh.value))) "
		+ "from TransactionProcess p "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
						+ "left join sf.warehouseLocation wh "
		+ "where "
			+ "p.id in :processIds "
		+ "group by p.id, item "
		+ "order by grp.ordinal ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByProcessIds(int[] processIds);
	
	

}
