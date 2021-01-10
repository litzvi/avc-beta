package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.dto.query.ItemCountWithAmount;
import com.avc.mis.beta.dto.query.ItemTransactionDifference;
import com.avc.mis.beta.entities.process.StorageTransfer;

public interface TransferRepository extends TransactionProcessRepository<StorageTransfer>{
	
	
	@Query("select new com.avc.mis.beta.dto.process.StorageTransferDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)) ) "
		+ "from StorageTransfer r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
		+ "where r.id = :processId ")
	Optional<StorageTransferDTO> findTransferDTOByProcessId(int processId);

	@Query("select new com.avc.mis.beta.dto.query.ItemTransactionDifference("
				+ "usedItem.id, usedItem.value, "
				+ "SUM(used_sf.unitAmount * used_sf.numberUnits * uom_used.multiplicand / uom_used.divisor), "
				+ "usedItem.measureUnit, "
				+ "SUM(sf.unitAmount * sf.numberUnits * uom_produced.multiplicand / uom_produced.divisor), "
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


	
	//already in ProcessRepository
//	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
//			+ " i.id, i.version, item.id, item.value, "
//			+ "poCode.code, ct.code, s.name, "
//			+ "sf.id, sf.version, "
//			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
//			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
//			+ "i.description, i.remarks) "
//		+ "from ProcessItem i "
//			+ "join i.item item "
//			+ "join i.process p "
//				+ "join p.poCode poCode "
//					+ "join poCode.contractType ct "
//					+ "join poCode.supplier s "
//			+ "join i.storageForms sf "
//				+ "join sf.unitAmount unit "
//				+ "left join sf.warehouseLocation warehouseLocation "
//		+ "where p.id = :processId ")
//	List<ProcessItemWithStorage> findProcessItemWithStorage(int processId);

//	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
//		+ " i.id, i.version, item.id, item.value, "
//		+ "poCode.code, ct.code, s.name, "
//		+ "sf.id, sf.version, "
//		+ "unit.amount, unit.measureUnit, sf.numberUnits, "
//		+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
//		+ "i.description, i.remarks) "
//	+ "from ProcessItem i "
//		+ "join i.item item "
//		+ "join i.process p "
//			+ "join p.poCode poCode "
//				+ "join poCode.contractType ct "
//				+ "join poCode.supplier s "
//		+ "join i.storageForms sf "
//			+ "join sf.unitAmount unit "
//			+ "left join sf.warehouseLocation warehouseLocation "
//	+ "where poCode.code = :poCodeId ")
//	List<ProcessItemWithStorage> findProcessItemWithStorageByPoCode(Integer poCodeId);
//	
//	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
//		+ " i.id, i.version, item.id, item.value, "
//		+ "poCode.code, ct.code, s.name, "
//		+ "sf.id, sf.version, "
//		+ "unit.amount, unit.measureUnit, sf.numberUnits, "
//		+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
//		+ "i.description, i.remarks) "
//	+ "from ProcessItem i "
//		+ "join i.item item "
//		+ "join i.process p "
//			+ "join p.poCode poCode "
//				+ "join poCode.contractType ct "
//				+ "join poCode.supplier s "
//		+ "join i.storageForms sf "
//			+ "join sf.unitAmount unit "
//			+ "left join sf.warehouseLocation warehouseLocation "
//	+ "where item.id = :itemId ")
//	List<ProcessItemWithStorage> findProcessItemWithStorageByItem(Integer itemId);

}
