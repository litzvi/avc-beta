/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.PoCode;

/**
 * @author Zvi
 *
 */
public interface InventoryRepository extends BaseRepository<PoCode> {


	@Query("select new com.avc.mis.beta.dto.report.ItemAmount( "
//			+ "pi.id, "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, "
//			+ "p.recordedTime, r.recordedTime, "
			+ "SUM((sf.unitAmount * uom.multiplicand / uom.divisor) "
//				+ " * item_unit.amount "
				+ " * "
				+ "(CASE "
					+ "WHEN ui is null THEN sf.numberUnits "
					+ "WHEN used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
						+ "THEN (sf.numberUnits / size(sf.usedItems) - ui.numberUnits) "
					+ "ELSE (sf.numberUnits / size(sf.usedItems)) "
				+ "END) "
				+ " - "
				+ "(CASE "
					+ "WHEN ui is null THEN coalesce(sf.accessWeight, 0) "
					+ "ELSE (coalesce(sf.accessWeight, 0) / size(sf.usedItems)) "
				+ "END)"
			+ " )  - coalesce(sf.accessWeight, 0) AS balance "
//			+ "(CASE type(item) "
//				+ "WHEN com.avc.mis.beta.entities.item.PackedItem THEN item_unit.measureUnit "
//				+ "ELSE item.measureUnit "
//			+ "END), "
//			+ "function('GROUP_CONCAT', sto.value)"
			+ ") "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
			+ "join UOM uom "
				+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join pi.process p "
				+ "join p.lifeCycle lc "
				+ "join p.poCode poCode "
//					+ "join poCode.contractType ct "
//					+ "join poCode.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = poCode "
						+ "join r.lifeCycle receipt_lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
//					+ "join UOM uom "
//						+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = item.measureUnit "
					+ "join sf_group.process sf_p "
						+ "join sf_p.lifeCycle sf_lc "
//				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and receipt_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and sf_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (poCode.id = :poCodeId or :poCodeId is null) "
			+ "and"
				+ "(sf.numberUnits > "
					+ "coalesce("
						+ "(select sum(usedStorage.numberUnits) "
						+ " from sf.usedItems usedStorage "
							+ "join usedStorage.group usedGroup "
								+ "join usedGroup.process usedProcess "
									+ "join usedProcess.lifeCycle usedLc "
						+ "where usedLc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
					+ ", 0)"
				+ ") "
		+ "group by item "
//		+ "order by r.recordedTime, p.recordedTime " 
		+ "")
	List<ItemAmount> findInventoryItemRows(boolean checkProductionUses, ProductionUse[] productionUses, ItemGroup itemGroup, Integer itemId, Integer poCodeId);

	
	/**
	 * Available inventory for querying what items are available for use by a process.
	 * Gets all information of items in the inventory that are available, for provided supply group, item or po code.
	 * If one of the parameters are null than will ignore that constraint.
	 * For each available item in inventory, provides information on the process item, storage and amounts used.
	 * Items are considered available inventory if the producing process status is final 
	 * and it's not completely used by another using process where the using process isn't cancelled.
	 * STORAGE IS CONSIDERED USED EVEN IF IT'S PENDING.
	 * @param supplyGroup constrain to only this supply group, if null than any.
	 * @param itemId constrain to only this item, if null than any.
	 * @param poCodeId constrain to only this po, if null than any.
	 * @return List of InventoryProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage( "
			+ "pi.id, "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, type(item), pi.measureUnit, "
			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, "
			+ "p.recordedTime, r.recordedTime, pi.tableView, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, sf.accessWeight, "
			+ "sto.id, sto.value, "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (ui IS NOT null AND used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN ui.numberUnits "
					+ "ELSE 0 "
				+ "END)"
			+ " ) AS total_used, "
			+ "(sf.unitAmount * uom.multiplicand / uom.divisor) "
				+ " * item_unit.amount "
				+ " * (sf.numberUnits - SUM("
					+ "(CASE "
						+ "WHEN (ui IS NOT null AND used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
							+ "THEN ui.numberUnits "
						+ "ELSE 0 "
					+ "END))) AS balance, "
			+ "(CASE "
			+ "WHEN type(item) = com.avc.mis.beta.entities.item.PackedItem THEN item_unit.measureUnit "
				+ "ELSE item.measureUnit "
			+ "END)) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
			+ "join UOM uom "
				+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = poCode "
						+ "join r.lifeCycle receipt_lc "
			+ "join p.lifeCycle lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
//					+ "join UOM uom "
//						+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = item.measureUnit "
					+ "join sf_group.process sf_p "
						+ "join sf_p.lifeCycle sf_lc "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and receipt_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and sf_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (poCode.id = :poCodeId or :poCodeId is null) "
		+ "group by sf "
		+ "having sf.numberUnits > "
			+ "SUM("
				+ "(CASE "
					+ "WHEN (ui IS NOT null AND used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
						+ "THEN ui.numberUnits "
					+ "ELSE 0 "
				+ "END)"
			+ " ) "
		+ "order by p.recordedTime, pi.ordinal, sf.ordinal ")
	List<InventoryProcessItemWithStorage> findAvailableInventoryProcessItemWithStorage(
			boolean checkProductionUses, ProductionUse[] productionUses, 
			ItemGroup itemGroup, Integer itemId, Integer poCodeId);

	
	@Query("select new com.avc.mis.beta.dto.view.ProcessItemInventoryRow( "
			+ "pi.id, "
			+ "item.id, item.value, item.productionUse, type(item), "
			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, "
			+ "p.recordedTime, r.recordedTime, "
			+ "SUM((sf.unitAmount * uom.multiplicand / uom.divisor) "
				+ " * item_unit.amount "
				+ " * "
				+ "(CASE "
					+ "WHEN ui is null THEN sf.numberUnits "
					+ "WHEN used_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
						+ "THEN (sf.numberUnits / size(sf.usedItems) - ui.numberUnits) "
					+ "ELSE (sf.numberUnits / size(sf.usedItems)) "
				+ "END) "
				+ " - "
				+ "(CASE "
				+ "WHEN ui is null THEN coalesce(sf.accessWeight, 0) "
					+ "ELSE (coalesce(sf.accessWeight, 0) / size(sf.usedItems)) "
				+ "END)"
			+ " )  - coalesce(sf.accessWeight, 0) AS balance, "
			+ "(CASE type(item) "
				+ "WHEN com.avc.mis.beta.entities.item.PackedItem THEN item_unit.measureUnit "
				+ "ELSE item.measureUnit "
			+ "END), "
			+ "function('GROUP_CONCAT', sto.value)) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
			+ "join UOM uom "
				+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join pi.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = poCode "
						+ "join r.lifeCycle receipt_lc "
				+ "join p.lifeCycle lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
//					+ "join UOM uom "
//						+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = item.measureUnit "
					+ "join sf_group.process sf_p "
						+ "join sf_p.lifeCycle sf_lc "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and receipt_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and sf_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (poCode.id = :poCodeId or :poCodeId is null) "
			+ "and"
				+ "(sf.numberUnits > "
					+ "coalesce("
						+ "(select sum(usedStorage.numberUnits) "
						+ " from sf.usedItems usedStorage "
							+ "join usedStorage.group usedGroup "
								+ "join usedGroup.process usedProcess "
									+ "join usedProcess.lifeCycle usedLc "
						+ "where usedLc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
					+ ", 0)"
				+ ") "
		+ "group by pi "
		+ "order by r.recordedTime, p.recordedTime " 
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
					+ "THEN ui.numberUnits "
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
	Stream<StorageBalance> findUsedStorageBalances(Integer processId);
	
	@Query("select new com.avc.mis.beta.dto.query.StorageBalance("
			+ "s.id, s.numberUnits, "
			+ "SUM("
			+ "(CASE "
				+ "WHEN (used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
					+ "THEN ui.numberUnits "
				+ "ELSE 0 "
			+ "END))) "
			+ "from ProcessWithProduct p "
				+ "join p.processItems pi "
					+ "join pi.storageForms s "
						+ "join s.usedItems ui "
							+ "join ui.group used_g "
								+ "join used_g.process used_p "
									+ "join used_p.lifeCycle used_lc "
			+ "where p.id = :processId "
			+ "group by s ")
	Stream<StorageBalance> findProducedStorageBalances(Integer processId);


	@Query("select new com.avc.mis.beta.dto.query.StorageBalance("
			+ "s.id, s.numberUnits, "
			+ "SUM("
			+ "(CASE "
				+ "WHEN (used_lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
					+ "THEN ui.numberUnits "
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
