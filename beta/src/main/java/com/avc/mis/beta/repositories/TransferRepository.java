package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.ItemTransactionDifference;
import com.avc.mis.beta.entities.process.StorageTransfer;

public interface TransferRepository extends TransactionProcessRepository<StorageTransfer>{
	

	@Query("select new com.avc.mis.beta.dto.query.ItemTransactionDifference("
				+ "usedItem.id, usedItem.value, "
				+ "SUM(coalesce(used_sf.unitAmount, 1) * used_sf.numberUnits * uom_used.multiplicand / uom_used.divisor), "
				+ "usedItem.measureUnit, "
				+ "SUM(coalesce(sf.unitAmount, 1) * sf.numberUnits * uom_produced.multiplicand / uom_produced.divisor), "
				+ "usedItem.measureUnit) "
			+ "from TransactionProcess p "
				+ "join p.usedItemGroups grp "
					+ "join grp.usedItems ui "
						+ "join ui.storage used_sf "
							+ "join used_sf.processItem usedPi "
								+ "join usedPi.item usedItem "
							+ "join used_sf.group used_group "
//								+ "join used_sf.unitAmount used_unit "
								+ "join UOM uom_used "
									+ "on uom_used.fromUnit = used_group.measureUnit and uom_used.toUnit = usedItem.measureUnit "						
				+ "left join ProcessItem pi "
					+ "on (pi.process = p and pi.item = usedItem) "
					+ "left join pi.storageForms sf "
						+ "left join sf.group produce_group "
//						+ "left join sf.unitAmount producedUnit "
						+ "left join UOM uom_produced "
							+ "on uom_produced.fromUnit = produce_group.measureUnit and uom_produced.toUnit = usedItem.measureUnit "
			+ "where p.id = :processId "
//				+ "and (pi is null or usedItem = producedItem) "
			+ "group by usedItem ")
	List<ItemTransactionDifference> findTransferDifferences(Integer processId);


}
