package com.avc.mis.beta.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.StorageTransferDTO;
import com.avc.mis.beta.entities.process.StorageTransfer;

public interface TransferRepository extends ProcessRepository<StorageTransfer>{
	

	@Query("select new com.avc.mis.beta.dto.process.StorageTransferDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
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
