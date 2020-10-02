/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.SampleReceiptDTO;
import com.avc.mis.beta.dto.query.SampleItemWithWeight;
import com.avc.mis.beta.entities.process.SampleReceipt;

/**
 * @author Zvi
 *
 */
public interface SampleRepository extends BaseRepository<SampleReceipt> {

	@Query("select new com.avc.mis.beta.dto.process.SampleReceiptDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from SampleReceipt r "
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
	Optional<SampleReceiptDTO> findSampleDTOByProcessId(int id);

//	@Query("select new com.avc.mis.beta.dto.process.SampleItemDTO( "
//			+ " i.id, i.version, item.id, item.value, "
//			+ "i.unitAmount, i.measureUnit, i.numberOfSamples, "
//			+ "i.avgTestedWeight, i.emptyContainerWeight) "
//		+ "from SampleItem i "
//			+ "join i.item item "
//			+ "join i.process p "
//		+ "where p.id = :processId ")
//	List<SampleItemDTO> findSampleItemsById(int processId);
	
	
	@Query("select new com.avc.mis.beta.dto.query.SampleItemWithWeight( "
			+ " i.id, i.version, i.ordinal, item.id, item.value, "
			+ "i.measureUnit, i.sampleContainerWeight, "
			+ "w.id, w.version, w.ordinal, w.unitAmount, w.numberUnits, w.numberOfSamples, w.avgTestedWeight) "
		+ "from SampleItem i "
			+ "join i.item item "
			+ "join i.process p "
			+ "join i.itemWeights w "
//			+ "join i.amountWeighed weighed "
		+ "where p.id = :processId "
		+ "order by i.ordinal, w.ordinal ")
	List<SampleItemWithWeight> findSampleItemsWithWeight(int processId);
}
