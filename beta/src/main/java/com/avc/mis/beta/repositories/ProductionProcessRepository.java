/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.processinfo.ProductWeightedPoDTO;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author zvi
 *
 */
public interface ProductionProcessRepository extends TransactionProcessRepository<ProductionProcess> {

	@Query("select new com.avc.mis.beta.dto.process.ProductionProcessDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from ProductionProcess r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
		+ "where r.id = :processId "
		+ "group by r ")
	Optional<ProductionProcessDTO> findProductionProcessDTOById(int processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.ProductWeightedPoDTO( "
			+ "weighted_po.id, weighted_po.version, weighted_po.ordinal, "
			+ "po_code.id, po_code.code, ct.code, ct.suffix, s.name, "
			+ "weighted_po.weight) "
		+ "from PoProcess p "
			+ "join p.productWeightedPos weighted_po "
				+ "join weighted_po.poCode po_code "
					+ "join po_code.contractType ct "
					+ "join po_code.supplier s "
		+ "where p.id = :processId "
		+ "order by weighted_po.ordinal ")
	List<ProductWeightedPoDTO> findProductWeightedPos(Integer processId);

	
	
}
