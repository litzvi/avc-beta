package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ProductionProcessDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.ProcessBasic;
import com.avc.mis.beta.entities.process.GeneralProcess;

/**
 * @author Zvi
 *
 */
public interface ProcessRepository<T extends GeneralProcess> extends BaseRepository<T> {

	
	@Query("select new com.avc.mis.beta.dto.process.ProductionProcessDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
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
		+ "where r.id = :processId ")
	Optional<ProductionProcessDTO> findProductionProcessDTOById(int processId);

	@Query("select new com.avc.mis.beta.dto.processinfo.UsedItemDTO( "
			+ "i.id, i.version, item.id, item.value, "
			+ "itemPo.id, ct.code, ct.suffix, s.name, "
			+ "unit.amount, unit.measureUnit, "
			+ "warehouseLocation.id, warehouseLocation.value, "
			+ "i.numberUnits) "
		+ "from UsedItem i "
			+ "join i.storage s "
				+ "join s.unitAmount unit "
				+ "left join s.warehouseLocation warehouseLocation "
				+ "join s.processItem pi "
					+ "join pi.item item "
			+ "join i.process p "
				+ "join p.poCode itemPo "
					+ "left join itemPo.contractType ct "
					+ "left join itemPo.supplier s "
		+ "where p.id = :processId ")
	Set<UsedItemDTO> findUsedItems(int processId);

	/**
	 * Gets the join of process item, process and storage information for the given process.
	 * @param processId id of the process
	 * @return List of ProcessItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.query.ProcessItemWithStorage( "
			+ " i.id, i.version, item.id, item.value, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "sf.id, sf.version, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, sf.containerWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks) "
		+ "from ProcessItem i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId ")
	List<ProcessItemWithStorage> findProcessItemWithStorage(int processId);

	/**
	 * Gets all processes done for given PoCode
	 * @param poCodeId id of PoCode
	 * @return List of ProcessBasic
	 */
	@Query("select new com.avc.mis.beta.dto.values.ProcessBasic( "
			+ "p.id, t.processName) "
		+ "from GeneralProcess p "
			+ "join p.poCode c "
			+ "join p.processType t "
		+ "where c.code = :poCodeId ")
	List<ProcessBasic> findAllProcessesByPo(Integer poCodeId);

}
