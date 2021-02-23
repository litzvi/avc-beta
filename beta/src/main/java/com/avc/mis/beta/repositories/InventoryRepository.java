/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.query.StorageBalance;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.codes.PoCode;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.NonNull;

/**
 * @author Zvi
 *
 */
public interface InventoryRepository extends BaseRepository<PoCode> {

	/**
	 * AVAILABLE STORAGE FOR USE - PRODUCT OF FINAL PROCESS NOT USED BY ANOTHER PROCESS (EVEN NOT FINAL)
	 */
	@Query("select new com.avc.mis.beta.dto.view.StorageInventoryRow( "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "pi.id, "
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
				+ "join p.lifeCycle lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
					+ "join sf_group.process sf_p "
						+ "join sf_p.lifeCycle sf_lc "
				+ "left join sf.warehouseLocation sto "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and sf_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and "
			+ "(:checkPoCodes = false "
			+ "or "
			+ "EXISTS "
					+ "(select r_po_code.id "
					+ " from pi.process p_2 "
						+ "left join p_2.poCode po_code "
						+ "left join p_2.weightedPos w_po "
							+ "left join w_po.poCode w_po_code "
						+ "join Receipt r "
							+ "on (r.poCode = po_code or r.poCode = w_po_code) "
							+ "join r.poCode r_po_code "
							+ "join r.lifeCycle receipt_lc "
					+ "where r_po_code.id in :poCodeIds "
						+ "and receipt_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL)) "
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
	List<StorageInventoryRow> findAvailableInventoryByStorage(
			boolean checkProductionUses, ProductionUse[] productionUses, 
			ItemGroup itemGroup, Integer itemId, 
			boolean checkPoCodes, Integer[] poCodeIds);
	
	@Query("select new com.avc.mis.beta.dto.view.ProcessItemInventory( "
			+ "pi.id, "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "pi.measureUnit, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "function('GROUP_CONCAT', concat(t.code, '-', po_code.code, coalesce(t.suffix, ''))), "
			+ "p.recordedTime, r.recordedTime, pi.tableView) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
			+ "join pi.process p "
				+ "left join p.poCode p_po_code "
					+ "left join p.weightedPos w_po "
						+ "left join w_po.poCode w_po_code "
					+ "join PoCode po_code "
						+ "on (po_code = p_po_code or po_code = w_po_code) "
						+ "join po_code.contractType t "
						+ "join po_code.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = po_code "
						+ "join r.lifeCycle receipt_lc "
		+ "where pi.id in :processItemIds "
		+ "group by pi "
		+ "order by p.recordedTime, pi.ordinal ")
	List<ProcessItemInventory> findProcessItemInventory(Set<Integer> processItemIds);

	/**
	 * INVENTORY FOR FINAL REPORT
	 */
	@Query("select new com.avc.mis.beta.dto.report.ItemAmount( "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((sf.unitAmount * uom.multiplicand / uom.divisor) "
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
			+ " ) AS balance, "
			+ "coalesce(w_po_code.weight, 1) "
			+ ") "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
			+ "join UOM uom "
				+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join pi.process p "
				+ "join p.lifeCycle lc "
				+ "left join p.poCode p_po_code "
				+ "left join p.weightedPos w_po_code "
				+ "join PoCode po_code "
					+ "on (po_code = p_po_code or po_code = w_po_code) "
				+ "join Receipt r "
					+ "on r.poCode = po_code "
					+ "join r.lifeCycle receipt_lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
					+ "join sf_group.process sf_p "
						+ "join sf_p.lifeCycle sf_lc "
				+ "left join sf.usedItems ui "
					+ "left join ui.group used_g "
						+ "left join used_g.process used_p "
							+ "left join used_p.lifeCycle used_lc "
		+ "where lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and receipt_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and sf_lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
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
		+ "group by item, w_po_code "
//		+ "order by r.recordedTime, p.recordedTime " 
		+ "")
	List<ItemAmount> findInventoryItemRows(boolean checkProductionUses, ProductionUse[] productionUses, ItemGroup itemGroup, Integer itemId, Integer poCodeId);


	
	/**
	 * LIST OF INVENTORY ITEMS FOR REPORT	 
	 */
	@Query("select new com.avc.mis.beta.dto.view.ProcessItemInventoryRow( "
			+ "pi.id, "
			+ "item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.name, "
			+ "p.recordedTime, r.recordedTime, "
			+ "coalesce(w_po.weight, 1), "
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
			+ " ) AS balance, "
//			+ "(CASE type(item) "
//				+ "WHEN com.avc.mis.beta.entities.item.PackedItem THEN item_unit.measureUnit "
//				+ "ELSE item.measureUnit "
//			+ "END), "
			+ "function('GROUP_CONCAT', sto.value)) "
		+ "from ProcessItem pi "
			+ "join pi.item item "
				+ "join item.unit item_unit "
			+ "join UOM uom "
				+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join pi.process p "
				+ "join p.lifeCycle lc "
				+ "left join p.poCode p_po_code "
				+ "left join p.weightedPos w_po "
					+ "left join w_po.poCode w_po_code "
					+ "join PoCode po_code "
						+ "on (po_code = p_po_code or po_code = w_po_code) "
						+ "join po_code.contractType t "
						+ "join po_code.supplier s "
					+ "join Receipt r "
						+ "on r.poCode = po_code "
						+ "join r.lifeCycle receipt_lc "
			+ "join pi.allStorages sf "
				+ "join sf.group sf_group "
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
			+ "and (:checkProductionUses = false or item.productionUse in :productionUses) "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (item.id = :itemId or :itemId is null) "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
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
		+ "group by pi, w_po "
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
