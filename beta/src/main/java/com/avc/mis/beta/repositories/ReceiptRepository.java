/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.query.ReceiptItemWithStorage;
import com.avc.mis.beta.dto.report.ReceiptItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.RecordStatus;
import com.avc.mis.beta.entities.process.Receipt;

/**
 * @author Zvi
 *
 */
public interface ReceiptRepository extends BaseRepository<Receipt> {

	@Query("select new com.avc.mis.beta.dto.process.ReceiptDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "lc.status, r.remarks) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
//			+ "left join r.status p_status "
		+ "where r.id = :id ")
	Optional<ReceiptDTO> findReceiptDTOByProcessId(int id);
	
	@Query("select new com.avc.mis.beta.dto.query.ReceiptItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks, "
			+ "oi.id, oi.version, extra.amount, extra.measureUnit) "
		+ "from ReceiptItem i "
			+ "left join i.orderItem oi "
			+ "join i.item item "
			+ "join i.process p "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
			+ "left join i.extraRequested extra "
		+ "where p.id = :processId ")
	List<ReceiptItemWithStorage> findReceiptItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.report.ReceiptItemRow( "
				+ "r.id, po_code.id, ct.code, s.name, i.value, "
				+ "units.amount, units.measureUnit, "
				+ "r.recordedTime, "
				+ "SUM(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor), i.measureUnit, "
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
						+ "join sf.unitAmount unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = i.measureUnit "
						+ "left join sf.warehouseLocation sto "
//					+ "left join ExtraAdded as added "
//						+ "on added.processItem = pi "
					+ "left join pi.orderItem oi "
						+ "left join oi.numberUnits units "
//					+ "left join pi.extraRequested extra "
			+ "join r.processType t "
//			+ "where type(sf) <> ExtraAdded "
				+ "where t.processName in :processNames "
				+ "and lc.status in :statuses "
			+ "group by r.id, oi, pi ")
	List<ReceiptItemRow> findAllReceiptsByType(ProcessName[] processNames, RecordStatus[] statuses);


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
