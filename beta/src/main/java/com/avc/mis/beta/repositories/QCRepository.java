/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.process.RawItemQualityDTO;
import com.avc.mis.beta.entities.process.QualityCheck;
/**
 * @author Zvi
 *
 */
public interface QCRepository extends BaseRepository<QualityCheck> {

	@Query("select new com.avc.mis.beta.dto.process.QualityCheckDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, po_code.contractType, s.id, s.version, s.name, "
			+ "r.processType, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "p_status, r.remarks) "
		+ "from QualityCheck r "
			+ "join r.poCode po_code "
			+ "join po_code.supplier s "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "left join r.status p_status "
		+ "where r.id = :id ")
	Optional<QualityCheckDTO> findQcByProcessId(int id);

	@Query("select new com.avc.mis.beta.dto.process.RawItemQualityDTO("
			+ "i.id, i.version, item, itemPo, i.unitAmount, i.measureUnit, i.numberUnits, "
			+ "storageLocation, i.description, i.remarks, "
			+ "i.breakage, i.foreignMaterial, i.humidity, i.testa, " 
			+ "i.scorched, i.deepCut, i.offColour, i.shrivel, i.desert, " 
			+ "i.deepSpot, i.mold, i.dirty, i.decay, i.insectDamage, " 
			+ "i.count, i.smallKernels, i.defectsAfterRoasting, i.weightLoss, " 
			+ "i.colour, i.flavour) "
		+ "from RawItemQuality i "
			+ "join i.item item "
			+ "join i.process p "
			+ "left join i.itemPo itemPo "
			+ "left join i.storageLocation storageLocation "
		+ "where p.id = :processId ")
	Set<RawItemQualityDTO> findCheckItemsById(int processId);

}
