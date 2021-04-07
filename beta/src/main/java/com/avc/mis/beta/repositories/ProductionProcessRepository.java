/**
 * 
 */
package com.avc.mis.beta.repositories;

import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author zvi
 *
 */
public interface ProductionProcessRepository extends TransactionProcessRepository<ProductionProcess> {

//	@Query("select new com.avc.mis.beta.dto.process.ProductionProcessDTO("
//			+ "r.id, r.version, r.createdDate, p_user.username, "
//			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name, po_code.display, "
//			+ "pt.processName, p_line, "
//			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
//			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
//		+ "from ProductionProcess r "
//			+ "join r.poCode po_code "
//				+ "join po_code.contractType t "
//				+ "join po_code.supplier s "
//			+ "join r.processType pt "
//			+ "join r.lifeCycle lc "
//			+ "left join r.createdBy p_user "
//			+ "left join r.productionLine p_line "
//			+ "left join r.approvals approval "
//				+ "left join approval.user u "
//		+ "where r.id = :processId "
//		+ "group by r ")
//	Optional<ProductionProcessDTO> findProductionProcessDTOById(int processId);


	
	
}
