/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.ProcessItemTransactionDifference;
import com.avc.mis.beta.entities.process.StorageRelocation;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.StorageBase;

/**
 * @author zvi
 *
 */
public interface RelocationRepository extends ProcessRepository<StorageRelocation>{

	/**
	 * @param processId
	 * @return
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemTransactionDifference("
			+ "pi.id, "
			+ "SUM(used_unit.amount * storageMove.numberUsedUnits * uom_used.multiplicand / uom_used.divisor), "
			+ "SUM(producedUnit.amount * storageMove.numberUnits * uom_produced.multiplicand / uom_produced.divisor), "
			+ "item.measureUnit) "
		+ "from StorageRelocation p "
			+ "join p.storageMoves storageMove "
				+ "join storageMove.processItem pi "
					+ "join pi.item item "
				+ "join storageMove.unitAmount producedUnit "
				+ "join UOM uom_produced "
					+ "on uom_produced.fromUnit = producedUnit.measureUnit and uom_produced.toUnit = item.measureUnit "
				+ "join storageMove.storage used_sf "
						+ "join used_sf.unitAmount used_unit "
						+ "join UOM uom_used "
							+ "on uom_used.fromUnit = used_unit.measureUnit and uom_used.toUnit = item.measureUnit "						
		+ "where p.id = :processId "
		+ "group by pi ")
	List<ProcessItemTransactionDifference> findRelocationDifferences(Integer processId);

	@Query("select s "
		+ "from Storage s "
		+ "where s.id in :storageIds ")
	Stream<Storage> findStoragesById(int[] storageIds);

}
