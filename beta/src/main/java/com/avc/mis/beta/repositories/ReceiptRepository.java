/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ProcessItemDTO;
import com.avc.mis.beta.dto.process.ReceiptDTO;
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

	@Query("select new com.avc.mis.beta.dto.process.ProcessItemDTO("
			+ "i.id, i.version, item, i.itemPo, i.unitAmount, i.measureUnit, i.numberUnits, "
			+ "i.storageLocation, i.description, i.remarks) "
		+ "from ProcessItem i "
		+ "left join i.item item "
		+ "join i.process p "
		+ "where p.id = :processId ")
	Set<ProcessItemDTO> findProcessItemsById(int processId);

	/**
	 * @param processId
	 * @return
	 */
//	Optional<ReceiptDTO> findReceiptByProcessId(int processId);

}
