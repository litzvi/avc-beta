/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.QualityCheckDTO;
import com.avc.mis.beta.dto.processinfo.CashewItemQualityDTO;
import com.avc.mis.beta.dto.values.CashewStandardDTO;
import com.avc.mis.beta.dto.values.ValueEntityObject;
import com.avc.mis.beta.dto.view.CashewQcRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.QualityCheck;
/**
 * @author Zvi
 *
 */
public interface QCRepository extends ProcessRepository<QualityCheck> {

	@Query("select new com.avc.mis.beta.dto.process.QualityCheckDTO("
			+ "r.id, r.version, r.inspector, r.sampleTaker, r.checkedBy, "
			+ "r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from QualityCheck r "
			+ "join r.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
				+ "left join r.approvals approval "
					+ "left join approval.user u "
		+ "where r.id = :id "
		+ "group by r ")
	Optional<QualityCheckDTO> findQcDTOByProcessId(int id);

	@Query("select new com.avc.mis.beta.dto.processinfo.CashewItemQualityDTO("
			+ "i.id, i.version, i.ordinal, item.id, item.value, "
			+ "i.measureUnit, i.sampleWeight, i.numberOfSamples, i.precentage, "
//			+ "i.description, i.remarks, "
			+ "i.wholeCountPerLb, i.smallSize, i.ws, i.lp, i.breakage, "
			+ "i.foreignMaterial, i.humidity, "
			+ "def.scorched, def.deepCut, def.offColour, def.shrivel, def.desert, def.deepSpot, "
			+ "dam.mold, dam.dirty, dam.lightDirty, dam.decay, dam.insectDamage, dam.testa, " 
			+ "i.roastingWeightLoss, " 
			+ "i.colour, i.flavour) "
		+ "from CashewItemQuality i "
			+ "join i.item item "
			+ "join i.process p "
			+ "join i.defects def "
			+ "join i.damage dam "
//			+ "join i.storageForms sf "
//				+ "left join sf.warehouseLocation warehouseLocation "
		+ "where p.id = :processId "
		+ "order by i.ordinal ")
	List<CashewItemQualityDTO> findCheckItemsById(int processId);

	//perhaps should be moved elsewhere
	@Query("select new com.avc.mis.beta.dto.values.CashewStandardDTO("
			+ "i.id, i.standardOrganization, "
			+ "i.totalDefects, i.totalDamage, i.totalDefectsAndDamage, "
			+ "i.wholeCountPerLb, i.smallSize, i.ws, i.lp, i.breakage, "
			+ "i.foreignMaterial, i.humidity, " 
			+ "def.scorched, def.deepCut, def.offColour, def.shrivel, def.desert, def.deepSpot, "
			+ "dam.mold, dam.dirty, dam.lightDirty, dam.decay, dam.insectDamage, dam.testa, " 
			+ "i.roastingWeightLoss) "
		+ "from CashewStandard i "
			+ "join i.items item "
			+ "join i.defects def "
			+ "join i.damage dam "
		+ "where item.id = :itemId and i.standardOrganization = :standardOrganization "
			+ "and i.active = true")
	CashewStandardDTO findCashewStandard(Integer itemId, String standardOrganization);
	
	@Query("select new com.avc.mis.beta.dto.values.CashewStandardDTO("
			+ "i.id, i.standardOrganization, "
			+ "i.totalDefects, i.totalDamage, i.totalDefectsAndDamage, "
			+ "i.wholeCountPerLb, i.smallSize, i.ws, i.lp, i.breakage, "
			+ "i.foreignMaterial, i.humidity, " 
			+ "def.scorched, def.deepCut, def.offColour, def.shrivel, def.desert, def.deepSpot, "
			+ "dam.mold, dam.dirty, dam.lightDirty, dam.decay, dam.insectDamage, dam.testa, " 
			+ "i.roastingWeightLoss) "
		+ "from CashewStandard i "
			+ "join i.items item "
			+ "join i.defects def "
			+ "join i.damage dam "
		+ "where i.active = true")
	List<CashewStandardDTO> findAllCashewStandardDTO();
	
	@Query("select new com.avc.mis.beta.dto.values.ValueEntityObject(i.id, item.id, item.value) "
		+ "from CashewStandard i "
			+ "join i.items item "
		+ "where i.active = true")
	Stream<ValueEntityObject<Item>> findAllStandardItems();


	@Query("select new com.avc.mis.beta.dto.view.CashewQcRow( "
			+ "qc.id, po_code.id, ct.code, ct.suffix, s.name, "
			+ "qc.checkedBy, i.id, i.value, qc.recordedTime, "
			+ "ti.numberOfSamples, ti.sampleWeight, ti.precentage, "
			+ "ti.humidity, ti.breakage,"
				+ "def.scorched, def.deepCut, def.offColour, "
				+ "def.shrivel, def.desert, def.deepSpot, "
				+ "dam.mold, dam.dirty, dam.lightDirty, "
				+ "dam.decay, dam.insectDamage, dam.testa) "
		+ "from QualityCheck qc "
			+ "join qc.testedItems ti "
				+ "join ti.item i "
				+ "join ti.defects def "
				+ "join ti.damage dam "
			+ "join qc.poCode po_code "
				+ "join po_code.supplier s "
				+ "join po_code.contractType ct "
			+ "join qc.processType pt "
		+ "where pt.processName in :processNames "
			+ "and (po_code.id = :poId or :poId is null) ")
	List<CashewQcRow> findCashewQualityChecks(ProcessName[] processNames, Integer poId);

		
	
//	@Query("select r "
//		+ "from QualityCheck r "
////			+ "join r.poCode po_code "
////			+ "join po_code.supplier s "
////			+ "left join r.createdBy p_user "
////			+ "left join r.productionLine p_line "
////			+ "left join r.status p_status "
//		+ "where r.id = :id ")
//	Optional<QualityCheck> findQcByProcessId(int id);

	
//	@Query("select new com.avc.mis.beta.dto.query.CashewItemQualityWithStorage( "
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
//		+ "from CashewItemQuality i "
//			+ "join i.item item "
//			+ "join i.process p "
//			+ "join i.storageForms sf "
//				+ "join sf.unitAmount unit "
//				+ "left join sf.warehouseLocation warehouseLocation "
//		+ "where p.id = :processId ")
//	Set<CashewItemQualityWithStorage> findCashewItemQualityWithStorage(int processId);

}
