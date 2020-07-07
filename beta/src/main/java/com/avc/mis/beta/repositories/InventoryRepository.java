/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemDTO;
import com.avc.mis.beta.dto.queryRows.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage;
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
			+ "SUM(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor), item.measureUnit) "
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

	@Query("select new com.avc.mis.beta.dto.process.StorageTransferDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "lc.status, r.remarks) "
		+ "from StorageTransfer r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
		+ "where r.id = :processId ")
	Optional<StorageTransferDTO> findTransferDTOByProcessId(int processId);
	

	@Query("select new com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "poCode.code, ct.code, s.name, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks) "
		+ "from ProcessItem i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId ")
	List<ProcessItemWithStorage> findProcessItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.UsedItemDTO( "
			+ "i.id, i.version, item.id, item.value, "
			+ "itemPo.id, ct.code, s.name, "
			+ "unit.amount, unit.measureUnit, "
			+ "warehouseLocation.id, warehouseLocation.value, "
			+ "i.numberUnits) "
		+ "from UsedItem i "
			+ "join i.storage s "
				+ "join s.unitAmount unit "
				+ "left join s.warehouseLocation warehouseLocation "
				+ "join s.processItem pi "
					+ "join pi.item item "
			+ "join i.process p "
				+ "join p.poCode itemPo "
					+ "left join itemPo.contractType ct "
					+ "left join itemPo.supplier s "
		+ "where p.id = :processId ")
	Set<UsedItemDTO> findUsedItems(int processId);

	@Query("select new com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "poCode.code, ct.code, s.name, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks) "
		+ "from ProcessItem i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where poCode.code = :poCodeId ")
	List<ProcessItemWithStorage> findProcessItemWithStorageByPoCode(Integer poCodeId);

	@Query("select new com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "poCode.code, ct.code, s.name, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks) "
		+ "from ProcessItem i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where item.id = :itemId ")
	List<ProcessItemWithStorage> findProcessItemWithStorageByItem(Integer itemId);

}
