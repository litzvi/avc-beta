/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.queryRows.InventoryProcessItemWithStorage;
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

	//using findInventoryProcessItemWithStorage
//	@Query("select new com.avc.mis.beta.dto.queryRows.ProcessItemInventoryRow( "
//			+ "pi.id, item.id, item.value, "
//			+ "poCode.code, ct.code, s.name, "
//			+ "p.recordedTime) "
////			+ "SUM(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor), "
////			+ "item.measureUnit) "
//		+ "from ProcessItem pi "
//			+ "join pi.item item "
//			+ "join pi.process p "
//				+ "join p.lifeCycle lc "
//				+ "join p.poCode poCode "
//					+ "join poCode.contractType ct "
//					+ "join poCode.supplier s "
//			+ "join pi.storageForms sf "
////				+ "join sf.unitAmount unit "
////					+ "join UOM uom "
////						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
//		+ "where lc.status = com.avc.mis.beta.entities.enums.RecordStatus.FINAL and "
//			+ "sf.numberUnits > coalesce((select sum(ui.numberUnits) from UsedItem ui where ui.storage = sf group by ui.storage), 0) and "
//			+ "(item.supplyGroup = :supplyGroup or :supplyGroup is null) and "
//			+ "(item.id = :itemId or :itemId is null) and "
//			+ "(poCode.code = :poCodeId or :poCodeId is null) "
//		+ "group by pi ")
////	fetch together with inventory storage..
//	List<ProcessItemInventoryRow> findInventoryProcessItem(SupplyGroup supplyGroup, Integer itemId, Integer poCodeId);

	//old before taking care of used items
//	@Query("select new com.avc.mis.beta.dto.queryRows.StorageInventoryRow( "
//			+ "sf.id, pi.id, unit.amount, unit.measureUnit, sf.numberUnits, "
//			+ "sto.id, sto.value) "
//		+ "from ProcessItem pi "
//			+ "join pi.item item "
//			+ "join pi.storageForms sf "
//				+ "join sf.unitAmount unit "
//				+ "left join sf.warehouseLocation sto "
//		+ "where item.supplyGroup = :supplyGroup ")
//	List<StorageInventoryRow> findInventoryStorage(SupplyGroup supplyGroup);
	
	//using findInventoryProcessItemWithStorage
//	@Query("select new com.avc.mis.beta.dto.queryRows.StorageInventoryRow( "
//			+ "sf.id, sf.version, pi.id, unit.amount, unit.measureUnit, sf.numberUnits, "
//			+ "sto.id, sto.value, "
//			+ "ui.numberUnits, "
//			+ "SUM(unit.amount * (sf.numberUnits - coalesce(ui.numberUnits, 0)) * uom.multiplicand / uom.divisor), "
//			+ "item.measureUnit) "
//		+ "from ProcessItem pi "
//			+ "join pi.item item "
//			+ "join pi.storageForms sf "
//				+ "join sf.unitAmount unit "
//					+ "join UOM uom "
//						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
//				+ "left join sf.warehouseLocation sto "
//				+ "left join sf.usedItems ui "
//		+ "where (item.supplyGroup = :supplyGroup or :supplyGroup is null) and "
//			+ "(pi.id in :processItemIds or coalesce(:processItemIds, null) is null ) "
//		+ "group by sf "
//		+ "having (ui.numberUnits is null or sf.numberUnits > sum(ui.numberUnits))")
//	List<StorageInventoryRow> findInventoryStorage(SupplyGroup supplyGroup, List<Integer> processItemIds);


	@Query("select new com.avc.mis.beta.dto.queryRows.InventoryProcessItemWithStorage( "
			+ "pi.id, "
			+ "item.id, item.value, "
			+ "poCode.code, ct.code, s.name, "
			+ "p.recordedTime, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "sto.id, sto.value, "
			+ "ui.numberUnits, "
			+ "SUM(unit.amount * (sf.numberUnits - coalesce(ui.numberUnits, 0)) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join p.lifeCycle lc "
			+ "join pi.storageForms sf "
				+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
		+ "where lc.status = com.avc.mis.beta.entities.enums.RecordStatus.FINAL and "
			+ "(item.supplyGroup = :supplyGroup or :supplyGroup is null) and "
			+ "(item.id = :itemId or :itemId is null) and "
			+ "(poCode.code = :poCodeId or :poCodeId is null) "
		+ "group by sf "
		+ "having (ui.numberUnits is null or sf.numberUnits > sum(coalesce(ui.numberUnits, 0))) ")
	List<InventoryProcessItemWithStorage> findInventoryProcessItemWithStorage(
			SupplyGroup supplyGroup, Integer itemId, Integer poCodeId);

}
