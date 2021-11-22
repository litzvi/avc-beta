/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.info.PoProcessInfo;
import com.avc.mis.beta.dto.query.ItemCountWithAmount;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.PoProcess;

/**
 * Spring repository for accessing all notification information and requirements of production processes.
 * 
 * @author Zvi
 *
 */
public interface PoProcessRepository<T extends PoProcess> extends ProcessRepository<T> {
	
	@Query("select new com.avc.mis.beta.dto.process.info.PoProcessInfo("
			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name) "
		+ "from PoProcess p "
			+ "join p.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
		+ "where type(p) = :clazz "
			+ "and p.id = :processId "
		+ "group by p ")
	Optional<PoProcessInfo> findPoProcessInfoByProcessId(int processId, Class<? extends T> clazz);

	
	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "("
				+ "SUM(count_amount.amount - coalesce(item_count.containerWeight, 0)) "
				+ "- coalesce(item_count.accessWeight, 0)"
			+ ") * uom.multiplicand / uom.divisor) "
		+ "from PoProcess p "
			+ "join p.itemCounts item_count "
				+ "join item_count.item item "
					+ "join item.unit item_unit "
				+ "join UOM uom "
					+ "on uom.fromUnit = item_count.measureUnit and uom.toUnit = item.measureUnit "
				+ "join item_count.amounts count_amount "
			+ "join p.processType pt "
		+ "where "
			+ "p.id in :processIds "
		+ "group by item_count ")
	Stream<ProductionProcessWithItemAmount> findAllItemsCountsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.query.ItemCountWithAmount( "
			+ " i.id, i.version, i.ordinal, "
			+ "item.id, item.value, item.productionUse, type(item), "
			+ "i.measureUnit, i.containerWeight, i.accessWeight, "
			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, "
			+ "count_amount.id, count_amount.version, count_amount.ordinal, count_amount.amount) "
		+ "from ItemCount i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.amounts count_amount "
		+ "where p.id = :processId "
		+ "order by i.ordinal, count_amount.ordinal ")
	List<ItemCountWithAmount> findItemCountWithAmount(int processId);

}
