/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.values.ReceiptItemWithStorage;
import com.avc.mis.beta.dto.values.ReceiptRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.Receipt;

/**
 * @author Zvi
 *
 */
public interface ReceiptRepository extends BaseRepository<Receipt> {

//	@Query("select r  "
//		+ "from Receipt r "
////			+ "join r.poCode po_code "
////			+ "join po_code.supplier s "
////			+ "left join r.createdBy p_user "
////			+ "left join r.productionLine p_line "
////			+ "left join r.status p_status "
//		+ "where r.id = :id ")
//	Optional<Receipt> findReceiptByProcessId(int id);
	
	@Query("select new com.avc.mis.beta.dto.process.ReceiptDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "p_status, r.remarks) "
		+ "from Receipt r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "left join r.status p_status "
		+ "where r.id = :id ")
	Optional<ReceiptDTO> findReceiptDTOByProcessId(int id);

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
	
	@Query("select new com.avc.mis.beta.dto.values.ReceiptItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "itemPo.id, ct.code, s.name, "
			+ "sf.id, sf.version, "
			+ "sf.unitAmount, sf.measureUnit, sf.numberUnits, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, "
			+ "i.description, i.remarks, oi.id, oi.version) "
		+ "from ReceiptItem i "
			+ "left join i.orderItem oi "
			+ "join i.item item "
			+ "join i.process p "
			+ "left join i.itemPo itemPo "
				+ "left join itemPo.contractType ct "
				+ "left join itemPo.supplier s "
			+ "join i.storageForms sf "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId ")
	List<ReceiptItemWithStorage> findReceiptItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.values.ReceiptRow( "
				+ "r.id, po_code.id, ct.code, s.name, i.value, "
				+ "oi.numberUnits, oi.measureUnit, "
				+ "r.recordedTime, SUM(sf.unitAmount * sf.numberUnits), sf.measureUnit, sto.value, "
				+ "added.amount, added.measureUnit) "
			+ "from Receipt r "
				+ "join r.poCode po_code "
					+ "join po_code.supplier s "
					+ "join po_code.contractType ct "
				+ "join r.processItems pi "
					+ "join pi.item i "
					+ "join pi.storageForms sf "
						+ "left join sf.warehouseLocation sto "
					+ "left join ExtraAdded as added "
						+ "on added.processItem = pi "
					+ "left join pi.orderItem oi "
			+ "join r.processType t "
			+ "where type(sf) <> ExtraAdded "
				+ "and t.processName in :processNames "
			+ "group by r.id, oi ")
	List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames);

	/**
	 * @param processId
	 * @return
	 */
//	Optional<ReceiptDTO> findReceiptByProcessId(int processId);

}
