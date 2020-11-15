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
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
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
			+ "item.id, item.value, item.productionUse, "
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
				+ " * item_unit.amount "
				+ " * (sf.numberUnits - SUM("
					+ "(CASE "
						+ "WHEN (ui IS NOT null AND used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
							+ "THEN ui.numberUsedUnits "
						+ "ELSE 0 "
					+ "END))) AS balance, "
//			+ "(unit.amount * (sf.numberUnits - total_used) * uom.multiplicand / uom.divisor), "
			+ "(CASE "
			+ "WHEN type(item) = com.avc.mis.beta.entities.item.PackedItem THEN item_unit.measureUnit "
				+ "ELSE item.defaultMeasureUnit "
			+ "END)) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
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
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.defaultMeasureUnit "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
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
			boolean checkProductionUses, ProductionUse[] productionUses, 
			ItemGroup itemGroup, Integer itemId, Integer poCodeId);

	
	@Query("select new com.avc.mis.beta.dto.view.ProcessItemInventoryRow( "
			+ "pi.id, "
			+ "item.id, item.value, item.productionUse, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "p.recordedTime, r.recordedTime, "
			+ "SUM(((unit.amount - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor) "
				+ " * item_unit.amount "
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
			+ "(CASE type(item) "
				+ "WHEN com.avc.mis.beta.entities.item.PackedItem THEN item_unit.measureUnit "
				+ "ELSE item.defaultMeasureUnit "
			+ "END), "
			+ "function('GROUP_CONCAT', sto.value)) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
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
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.defaultMeasureUnit "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
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
			boolean checkProductionUses, ProductionUse[] productionUses, ItemGroup itemGroup, Integer itemId, Integer poCodeId);

	
	/**
	 * Gets the storage balance for storages used by the given process.
	 * @param processId id of the process
	 * @return Stream of StorageBalance
	 */
	@Query("select new com.avc.mis.beta.dto.query.StorageBalance("
			+ "s.id, s.numberUnits, "
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

}
