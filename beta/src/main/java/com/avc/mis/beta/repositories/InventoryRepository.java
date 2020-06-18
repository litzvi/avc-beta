/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.queryRows.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.queryRows.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface InventoryRepository extends BaseRepository<PoCode> {

//	@Query("select new com.avc.mis.beta.dto.values.PoInventoryRowWithStorage( "
//			+ "pi.id, pi.version, item.id, item.value, "
//			+ "poCode.code, ct.code, s.name, "
//			+ "sf.id, sf.version, "
//			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
//			+ "warehouseLocation.id, warehouseLocation.value, "
//			+ "pi.description, sf.remarks, type(sf) "
//			+ ") "
//		+ "from ProcessItem pi "
//			+ "join pi.item item "
//			+ "join pi.process p "
//				+ "join p.poCode poCode "
//					+ "join poCode.contractType ct "
//					+ "join poCode.supplier s "
//			+ "pi.storageForms sf "
//				+ "join sf.unitAmount unit "
//				+ "left join sf.warehouseLocation warehouseLocation "
//		+ "where item.supplyGroup = ?1 ")
//	List<PoInventoryRowWithStorage> findInventoryTable(SupplyGroup supplyGroup);
	
	@Query("select new com.avc.mis.beta.dto.queryRows.ProcessItemInventoryRow( "
			+ "pi.id, item.id, item.value, "
			+ "poCode.code, ct.code, s.name, "
			+ "p.recordedTime, "
			+ "SUM(unit.amount * sf.numberUnits * uom.multiply / uom.divide), item.measureUnit) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.process p "
				+ "join p.lifeCycle lc "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join pi.storageForms sf "
				+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
		+ "where item.supplyGroup = ?1 and "
			+ "lc.status = com.avc.mis.beta.entities.enums.RecordStatus.FINAL "
		+ "group by pi ")
	List<ProcessItemInventoryRow> findInventoryProcessItem(SupplyGroup supplyGroup);
	
	@Query("select new com.avc.mis.beta.dto.queryRows.StorageInventoryRow( "
			+ "sf.id, pi.id, unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "sto.id, sto.value) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation sto "
		+ "where item.supplyGroup = ?1 ")
	List<StorageInventoryRow> findInventoryStorage(SupplyGroup supplyGroup);
}
