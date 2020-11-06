/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface InventoryRepository extends BaseRepository<PoCode> {


	/**
	 * Gets all information of items in the inventory, for provided supply group, item or po code.
	 * If one of the parameters are null than will ignore that constraint.
	 * For each stored item in inventory, provides information on the process item, storage and amounts used.
	 * Items are considered in inventory if process status is final and it's not completely used.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @param poCodeId constrain to only this po, if null than any.
	 * @return List of InventoryProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage( "
			+ "pi.id, "
			+ "item.id, item.value, item.category, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "p.recordedTime, r.recordedTime, pi.tableView, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, sf.containerWeight, "
			+ "sto.id, sto.value, "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (ui IS NOT null AND used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
						+ "THEN ui.numberUsedUnits "
					+ "ELSE 0 "
				+ "END)"
			+ " ) AS total_used, "
//			+ "sum(coalesce(ui.numberUsedUnits, 0)), "
//			+ "unit.amount * uom.multiplicand / uom.divisor) * (sf.numberUnits - SUM(coalesce(ui.numberUsedUnits, 0)))"
			+ "(unit.amount * uom.multiplicand / uom.divisor) "
				+ " * (sf.numberUnits - SUM("
					+ "(CASE "
						+ "WHEN (ui IS NOT null AND used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
							+ "THEN ui.numberUsedUnits "
						+ "ELSE 0 "
					+ "END))) AS balance, "
//			+ "(unit.amount * (sf.numberUnits - total_used) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = poCode "
			+ "join p.lifeCycle lc "
			+ "join pi.allStorages sf "
				+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.supplyGroup = :supplyGroup or :supplyGroup is null) "
			+ "and (:checkCategories = false or item.category in :itemCategories) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (poCode.code = :poCodeId or :poCodeId is null) "
		+ "group by sf "
//		+ "having sf.numberUnits > total_used "
//		+ "having (sf.numberUnits > sum(coalesce(ui.numberUsedUnits, 0))) "
		+ "having sf.numberUnits > "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (ui IS NOT null AND used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
						+ "THEN ui.numberUsedUnits "
					+ "ELSE 0 "
				+ "END)"
			+ " ) "
		+ "order by p.recordedTime, pi.ordinal, sf.ordinal ")
	List<InventoryProcessItemWithStorage> findInventoryProcessItemWithStorage(
			boolean checkCategories, ItemCategory[] itemCategories, 
			SupplyGroup supplyGroup, Integer itemId, Integer poCodeId);

	
	@Query("select new com.avc.mis.beta.dto.view.ProcessItemInventoryRow( "
			+ "pi.id, "
			+ "item.id, item.value, item.category, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "p.recordedTime, r.recordedTime, "
			+ "SUM((unit.amount - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor "
				+ " * "
				+ "(CASE "
					+ "WHEN ui is null THEN sf.numberUnits "
					+ "WHEN used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
						+ "THEN (sf.numberUnits / size(sf.usedItems) - ui.numberUsedUnits) "
					+ "ELSE (sf.numberUnits / size(sf.usedItems)) "
				+ "END)"
			+ " ) AS balance, "
//			+ "SUM((unit.amount - coalesce(sf.containerWeight, 0)) * sf.numberUnits / size(sf.usedItems) * uom.multiplicand / uom.divisor), "
//			+ "SUM((unit.amount - coalesce(sf.containerWeight, 0)) * coalesce(ui.numberUsedUnits, 0) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, "
			+ "function('GROUP_CONCAT', sto.value)) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = poCode "
			+ "join p.lifeCycle lc "
			+ "join pi.allStorages sf "
				+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.supplyGroup = :supplyGroup or :supplyGroup is null) "
			+ "and (:checkCategories = false or item.category in :itemCategories) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (poCode.code = :poCodeId or :poCodeId is null) "
//			+ "and (not exists "
//				+ "(select usedStorage "
//				+ " from sf.usedItems usedStorage) "
//		 	+ "and ((sf.usedItems is empty) "
//				+ "or "
			+ "and"
				+ "(sf.numberUnits > "
					+ "coalesce("
						+ "(select sum(usedStorage.numberUsedUnits) "
						+ " from sf.usedItems usedStorage "
							+ "join usedStorage.group usedGroup "
								+ "join usedGroup.process usedProcess "
									+ "join usedProcess.lifeCycle usedLc "
						+ "where usedLc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
					+ ", 0)"
				+ ") "
		+ "group by pi "
//		+ "having "
//			+ "SUM((unit.amount - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor "
//				+ "* "
//				+ "(CASE "
//					+ "WHEN ui is null THEN sf.numberUnits "
//					+ "WHEN used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
//						+ "THEN (sf.numberUnits / size(sf.usedItems) - ui.numberUsedUnits) "
//					+ "ELSE (sf.numberUnits / size(sf.usedItems)) "
//				+ "END)"
//			+ " ) > 0 "
//		+ "having (SUM(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) "
//			+ "> SUM(unit.amount * coalesce(ui.numberUsedUnits, 0) * uom.multiplicand / uom.divisor)) "
		+ "order by r.recordedTime " 
		+ "")
	List<ProcessItemInventoryRow> findInventoryProcessItemRows(
			boolean checkCategories, ItemCategory[] itemCategories, SupplyGroup supplyGroup, Integer itemId, Integer poCodeId);

	
	/**
	 * Gets the storage balance for storages used by the given process.
	 * @param processId id of the process
	 * @return Stream of StorageBalance
	 */
	@Query("select new com.avc.mis.beta.dto.query.StorageBalance("
			+ "s.id, s.numberUnits, "
//			+ "sum(ui.numberUsedUnits) "
			+ "SUM("
			+ "(CASE "
				+ "WHEN (used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
					+ "THEN ui.numberUsedUnits "
				+ "ELSE 0 "
			+ "END))) "
			+ "from TransactionProcess p "
				+ "join p.usedItemGroups grp "
					+ "join grp.usedItems i "
						+ "join i.storage s "
							+ "join s.usedItems ui "
								+ "join ui.group used_g "
									+ "join used_g.process used_p "
										+ "join used_p.lifeCycle used_lc "
			+ "where p.id = :processId "
			+ "group by s ")
	Stream<StorageBalance> findStorageBalances(Integer processId);


	@Query("select new com.avc.mis.beta.dto.query.StorageBalance("
			+ "s.id, s.numberUnits, "
			+ "SUM("
			+ "(CASE "
				+ "WHEN (used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
					+ "THEN ui.numberUsedUnits "
				+ "ELSE 0 "
			+ "END))) "
			+ "from StorageRelocation p "
				+ "join p.storageMovesGroups g "
					+ "join g.storageMoves storageMove "
						+ "join storageMove.storage s "
							+ "left join s.usedItems ui "
								+ "join ui.group used_g "
									+ "join used_g.process used_p "
										+ "join used_p.lifeCycle used_lc "
			+ "where p.id = :processId "
			+ "group by s ")
	Stream<StorageBalance> findStorageMoveBalances(Integer processId);


	
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
//	@Query("select new com.avc.mis.beta.dto.query.ProcessItemInventoryRow( "
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
//			+ "sf.numberUnits > coalesce((select sum(ui.numberUsedUnits) from UsedItem ui where ui.storage = sf group by ui.storage), 0) and "
//			+ "(item.supplyGroup = :supplyGroup or :supplyGroup is null) and "
//			+ "(item.id = :itemId or :itemId is null) and "
//			+ "(poCode.code = :poCodeId or :poCodeId is null) "
//		+ "group by pi ")
////	fetch together with inventory storage..
//	List<ProcessItemInventoryRow> findInventoryProcessItem(SupplyGroup supplyGroup, Integer itemId, Integer poCodeId);

	//old before taking care of used items
//	@Query("select new com.avc.mis.beta.dto.query.StorageInventoryRow( "
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
//	@Query("select new com.avc.mis.beta.dto.query.StorageInventoryRow( "
//			+ "sf.id, sf.version, pi.id, unit.amount, unit.measureUnit, sf.numberUnits, "
//			+ "sto.id, sto.value, "
//			+ "ui.numberUsedUnits, "
//			+ "SUM(unit.amount * (sf.numberUnits - coalesce(ui.numberUsedUnits, 0)) * uom.multiplicand / uom.divisor), "
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
//		+ "having (ui.numberUsedUnits is null or sf.numberUnits > sum(ui.numberUnits))")
//	List<StorageInventoryRow> findInventoryStorage(SupplyGroup supplyGroup, List<Integer> processItemIds);

}
