/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ReceiptDTO;
import com.avc.mis.beta.dto.process.ReceiptItemDTO;
import com.avc.mis.beta.dto.values.ReceiptRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.Receipt;

/**
 * @author Zvi
 *
 */
public interface ReceiptRepository extends BaseRepository<Receipt> {

	@Query("select new com.avc.mis.beta.dto.process.ReceiptDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code, r.processType, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "p_status, r.remarks) "
		+ "from Receipt r "
			+ "left join r.poCode po_code "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "left join r.status p_status "
		+ "where r.id = :id ")
	Optional<ReceiptDTO> findReceiptByProcessId(int id);

	@Query("select new com.avc.mis.beta.dto.process.ReceiptItemDTO("
			+ "i.id, i.version, item, itemPo, i.unitAmount, i.measureUnit, i.numberUnits, "
			+ "storageLocation, i.description, i.remarks, oi.id, oi.version) "
		+ "from ReceiptItem i "
			+ "left join i.orderItem oi "
			+ "join i.item item "
			+ "join i.process p "
			+ "left join i.itemPo itemPo "
			+ "left join i.storageLocation storageLocation "
		+ "where p.id = :processId ")
	Set<ReceiptItemDTO> findReceiptItemsById(int processId);

	@Query("select new com.avc.mis.beta.dto.values.ReceiptRow( "
			+ "r.id, po_code, s.name, i.value, "
			+ "oi.numberUnits, " 
			+ "oi.measureUnit, r.recordedTime, SUM(pi.unitAmount * pi.numberUnits), pi.measureUnit, "
			+ "sto.value) "
			+ "from Receipt r "
				+ "join r.poCode po_code "
				+ "join r.supplier s "
				+ "join r.processItems pi "
					+ "join pi.item i "
					+ "join pi.storageLocation sto "
					+ "left join pi.orderItem oi "
			+ "join r.processType t "
			+ "where t.processName in :processNames "
			+ "group by r.id, oi ")
	List<ReceiptRow> findAllReceiptsByType(ProcessName[] processNames);

	/**
	 * @param processId
	 * @return
	 */
//	Optional<ReceiptDTO> findReceiptByProcessId(int processId);

}
