/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.processinfo.RawItemQualityDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.QualityCheck;
/**
 * @author Zvi
 *
 */
public interface QCRepository extends ProcessRepository<QualityCheck> {
	
//	@Query("select r "
//		+ "from QualityCheck r "
////			+ "join r.poCode po_code "
////			+ "join po_code.supplier s "
////			+ "left join r.createdBy p_user "
////			+ "left join r.productionLine p_line "
////			+ "left join r.status p_status "
//		+ "where r.id = :id ")
//	Optional<QualityCheck> findQcByProcessId(int id);

	@Query("select new com.avc.mis.beta.dto.process.QualityCheckDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "lc.status, r.remarks) "
		+ "from QualityCheck r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
//			+ "left join r.status p_status "
		+ "where r.id = :id ")
	Optional<QualityCheckDTO> findQcDTOByProcessId(int id);

	@Query("select new com.avc.mis.beta.dto.processinfo.RawItemQualityDTO("
			+ "i.id, i.version, item.id, item.value, "
			+ "i.measureUnit, i.sampleWeight, "
//			+ "i.description, i.remarks, "
			+ "i.wholeCountPerLb, i.smallSize, i.ws, i.lp, i.breakage, "
			+ "i.foreignMaterial, i.humidity, i.testa, " 
			+ "i.scorched, i.deepCut, i.offColour, i.shrivel, i.desert, " 
			+ "i.deepSpot, i.mold, i.dirty, i.decay, i.insectDamage, " 
			+ "i.roastingWeightLoss, " 
			+ "i.colour, i.flavour) "
		+ "from RawItemQuality i "
			+ "join i.item item "
			+ "join i.process p "
//			+ "join i.storageForms sf "
//				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId ")
	Set<RawItemQualityDTO> findCheckItemsById(int processId);

	//perhaps should be moved elsewhere
	@Query("select new com.avc.mis.beta.dto.values.CashewStandardDTO("
			+ "i.id, i.standardOrganization, item.id, item.value, "
			+ "i.totalDefects, i.totalDamage, "
			+ "i.wholeCountPerLb, i.smallSize, i.ws, i.lp, i.breakage, "
			+ "i.foreignMaterial, i.humidity, i.testa, " 
			+ "i.scorched, i.deepCut, i.offColour, i.shrivel, i.desert, " 
			+ "i.deepSpot, i.mold, i.dirty, i.decay, i.insectDamage, " 
			+ "i.roastingWeightLoss) "
		+ "from CashewStandard i "
			+ "join i.item item "
		+ "where item.id = :itemId and i.standardOrganization = :standardOrganization "
			+ "and i.active = true")
	CashewStandardDTO findCashewStandard(Integer itemId, String standardOrganization);
	
//	@Query("select new com.avc.mis.beta.dto.query.RawItemQualityWithStorage( "
//			+ " i.id, i.version, item.id, item.value, "
//			+ "sf.id, sf.version, "
//			+ "unit.amount, unit.measureUnit, sf.numberUnits, "
//			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, "
//			+ "i.description, i.remarks, type(sf), "
//			+ "i.breakage, i.foreignMaterial, i.humidity, i.testa, "
//			+ "i.scorched, i.deepCut, i.offColour, i.shrivel, i.desert, "
//			+ "i.deepSpot, i.mold, i.dirty, i.decay, i.insectDamage, "
//			+ "i.nutCount, i.smallKernels, i.defectsAfterRoasting, i.weightLoss, "
//			+ "i.colour, i.flavour) "
//		+ "from RawItemQuality i "
//			+ "join i.item item "
//			+ "join i.process p "
//			+ "join i.storageForms sf "
//				+ "join sf.unitAmount unit "
//				+ "left join sf.warehouseLocation warehouseLocation "
//		+ "where p.id = :processId ")
//	Set<RawItemQualityWithStorage> findRawItemQualityWithStorage(int processId);

}
