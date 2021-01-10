/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.query.ReceiptItemWithStorage;
import com.avc.mis.beta.dto.view.ReceiptItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;

/**
 * @author Zvi
 *
 */
public interface ReceiptRepository extends ProcessWithProductRepository<Receipt> {

	@Query("select new com.avc.mis.beta.dto.process.ReceiptDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
		+ "where r.id = :id ")
	Optional<ReceiptDTO> findReceiptDTOByProcessId(int id);
	
	@Query("select new com.avc.mis.beta.dto.query.ReceiptItemWithStorage( "
			+ " i.id, i.version, i.ordinal, item.id, item.value, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), sf_group.measureUnit, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, sf.accessWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "sf.sampleContainerWeights, sf.sampleWeights, sf.numberOfSamples, sf.avgTestedWeight, "
//			+ "CASE "
//				+ "WHEN type(sf) = Storage THEN sf.emptyContainerWeight  "
//				+ "ELSE null "
//			+ "END , "
//			+ "CASE "
//				+ "WHEN type(sf) = Storage THEN sf.numberOfSamples  "
//				+ "ELSE null "
//			+ "END , "
//			+ "CASE "
//				+ "WHEN type(sf) = Storage THEN sf.avgTestedWeight  "
//				+ "ELSE null "
//			+ "END , "
			+ "i.groupName, i.description, i.remarks, i.tableView, "
			+ "ru.amount, ru.measureUnit, up.amount, up.currency, "
			+ "oi.id, oi.version, extra.amount, extra.measureUnit) "
		+ "from ReceiptItem i "
			+ "left join i.orderItem oi "
			+ "join i.item item "
				+ "join item.unit item_unit "
			+ "left join i.receivedOrderUnits ru "
			+ "left join i.unitPrice up "
			+ "join i.process p "
			+ "join i.storageForms sf "
				+ "join sf.group sf_group "
				+ "left join sf.warehouseLocation warehouseLocation "
			+ "left join i.extraRequested extra "
		+ "where p.id = :processId "
		+ "order by i.ordinal, sf.ordinal, sf.dtype "
//		+ ", sf.ordinal " //already sorted in dto for storages
		+ "")
	List<ReceiptItemWithStorage> findReceiptItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ReceiptItemRow( "
				+ "r.id, po_code.id, po_code.code, ct.code, ct.suffix, s.name, "
				+ "i.id, i.value, "
				+ "units.amount, units.measureUnit, "
				+ "r.recordedTime, lc.processStatus, "
				+ "SUM(sf.unitAmount * sf.numberUnits * uom.multiplicand / uom.divisor), i.measureUnit, "
				+ "function('GROUP_CONCAT', sto.value), "
				+ "extra.amount, extra.measureUnit) "
			+ "from Receipt r "
				+ "join r.lifeCycle lc "
				+ "join r.poCode po_code "
					+ "join po_code.supplier s "
					+ "join po_code.contractType ct "
				+ "join r.processItems pi "
					+ "left join pi.extraRequested extra "
					+ "join pi.item i "
					+ "join pi.storageForms sf "
						+ "left join sf.warehouseLocation sto "
//						+ "join sf.group sf_group "
				+ "join UOM uom "
					+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = i.measureUnit "
//					+ "left join ExtraAdded as added "
//						+ "on added.processItem = pi "
					+ "left join pi.orderItem oi "
						+ "left join oi.numberUnits units "
//					+ "left join pi.extraRequested extra "
				+ "join r.processType t "
//			+ "where type(sf) <> ExtraAdded "
			+ "where t.processName in :processNames "
				+ "and lc.processStatus in :statuses "
				+ "and (po_code.id = :poCodeId or :poCodeId is null)"
			+ "group by r.id, oi, pi "
			+ "order by r.recordedTime desc ")
	List<ReceiptItemRow> findAllReceiptsByType(ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId);


//	@Query("select r  "
//		+ "from Receipt r "
////			+ "join r.poCode po_code "
////			+ "join po_code.supplier s "
////			+ "left join r.createdBy p_user "
////			+ "left join r.productionLine p_line "
////			+ "left join r.status p_status "
//		+ "where r.id = :id ")
//	Optional<Receipt> findReceiptByProcessId(int id);
	

//	@Query("select new com.avc.mis.beta.dto.process.ReceiptItemDTO("
//			+ "i.id, i.version, item.id, item.value, "
//			+ "itemPo.id, ct.code, s.name, "
//			+ "i.description, i.remarks, oi.id, oi.version) "
//		+ "from ReceiptItem i "
//			+ "left join i.orderItem oi "
//			+ "join i.item item "
//			+ "join i.process p "
//			+ "left join i.itemPo itemPo "
//				+ "join itemPo.contractType ct "
//				+ "join itemPo.supplier s "
////			+ "left join i.storageLocation storageLocation "
//		+ "where p.id = :processId ")
//	Set<ReceiptItemDTO> findReceiptItemsById(int processId);

	
	/**
	 * @param processId
	 * @return
	 */
//	Optional<ReceiptDTO> findReceiptByProcessId(int processId);

}
