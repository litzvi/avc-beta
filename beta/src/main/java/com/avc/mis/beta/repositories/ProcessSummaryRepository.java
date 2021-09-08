/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.avc.mis.beta.dto.query.ItemAmountWithLoadingReportLine;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.report.ItemQc;
import com.avc.mis.beta.dto.report.ProcessStateInfo;
import com.avc.mis.beta.dto.report.QcReportLine;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.QcCompany;
import com.avc.mis.beta.entities.item.ProductionUse;

/**
 * @author zvi
 *
 */
@RepositoryDefinition(domainClass = BaseEntity.class, idClass = Integer.class)
public interface ProcessSummaryRepository {

	@Query("select new com.avc.mis.beta.dto.report.ProcessStateInfo("
			+ "p.id, p.recordedTime, lc.processStatus, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ': ', approval.decision))) ) "
		+ "from PoProcess p "
			+ "left join p.poCode po_code "
			+ "left join p.weightedPos w_po "
				+ "left join w_po.poCode w_po_code "
			+ "join p.processType pt "
			+ "join p.lifeCycle lc "
			+ "left join p.approvals approval "
				+ "left join approval.user u "
		+ "where pt.processName = :processName "
			+ "and ("
				+ "(coalesce(po_code.id, w_po_code.id) = :poCodeId) "
				+ "or :poCodeId is null "
			+ ") "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
		+ "group by p "
		+ "order by p.recordedTime desc ")
	List<ProcessStateInfo> findProcessReportLines(ProcessName processName, Integer poCodeId, boolean cancelled);

	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM("
				+ "((ui.numberUnits * coalesce(sf.unitAmount, 1)) * uom.multiplicand / uom.divisor) "
				+ " * coalesce(w_po.weight, 1))"
			+ ") "
		+ "from TransactionProcess p "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
							+ ""
			+ "left join p.poCode p_po_code "
			+ "left join p.weightedPos w_po "
				+ "left join w_po.poCode w_po_code "
//				+ "join BasePoCode po_code "
//					+ "on (po_code = p_po_code or po_code = w_po_code) "
//							+ "join pi.process u_p "
//								+ "left join u_p.poCode p_po_code "
//								+ "left join u_p.weightedPos w_po "
//									+ "left join w_po.poCode w_po_code "
//									+ "join BasePoCode po_code "
//										+ "on (po_code = p_po_code or po_code = w_po_code) "
		+ "where "
			+ "p.id in :processIds "
			+ "and ("
				+ "(coalesce(p_po_code.id, w_po_code.id) = :poCodeId) "
				+ "or :poCodeId is null "
			+ ") "
		+ "group by item.id ")
	Stream<ItemAmount> findSummaryUsedItemAmounts(int[] processIds, Integer poCodeId);
	
	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM( "
				+ "((sf.numberUnits * coalesce(sf.unitAmount, 1)) * uom.multiplicand / uom.divisor) "
//				+ " * coalesce(w_po.weight, 1))"
				+ "), "
			+ "coalesce(w_po.weight, 1) "
			+ ")"
		+ "from PoProcess p "
			+ "join p.processItems pi "
				+ "join pi.item item "
					+ "join item.unit item_unit "
				+ "join pi.storageForms sf "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join p.poCode p_po_code "
				+ "left join p.weightedPos w_po "
					+ "left join w_po.poCode w_po_code "
//					+ "join BasePoCode po_code "
//						+ "on (po_code = p_po_code or po_code = w_po_code) "
		+ "where "
			+ "p.id in :processIds "
			+ "and ("
				+ "(coalesce(p_po_code.id, w_po_code.id) = :poCodeId) "
				+ "or :poCodeId is null "
			+ ") "
		+ "group by item.id, w_po.weight ")
	Stream<ItemAmount> findSummaryProducedItemAmounts(int[] processIds, Integer poCodeId);
	
	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "("
				+ "SUM(count_amount.amount - coalesce(item_count.containerWeight, 0)) "
				+ "- coalesce(item_count.accessWeight, 0)"
			+ ") * uom.multiplicand / uom.divisor) "
		+ "from PoProcess p "
			+ "join p.itemCounts item_count "
				+ "join item_count.item item "
					+ "join item.unit item_unit "
				+ "join UOM uom "
					+ "on uom.fromUnit = item_count.measureUnit and uom.toUnit = item.measureUnit "
				+ "join item_count.amounts count_amount "
		+ "where p.id = :processId "
			+ "and item.id in :productItemsIds "
		+ "group by item_count, item.id ")
	List<ItemAmount> findProductCountItemAmountsByProcessId(Integer processId, int[] productItemsIds);

	@Query("select new com.avc.mis.beta.dto.report.QcReportLine( "
			+ "qc.id, qc.checkedBy, "
			+ "qc.recordedTime, "
			+ "lc.processStatus, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ': ', approval.decision))) ) "
		+ "from QualityCheck qc "
			+ "join qc.poCode po_code "
			+ "join qc.processType pt "
			+ "join qc.lifeCycle lc "
			+ "left join qc.approvals approval "
				+ "left join approval.user u "
		+ "where pt.processName = :processName "
			+ "and po_code.id = :poId "
			+ "and qc.checkedBy = :qcCompany "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
		+ "group by qc "
		+ "order by qc.recordedTime desc ")
	List<QcReportLine> findCashewQCReportLines(ProcessName processName, Integer poId, QcCompany qcCompany, boolean cancelled);

	@Query("select new com.avc.mis.beta.dto.report.ItemQc( "
			+ "qc.id, po_code.id, "
			+ "i.id, i.value, i.productionUse, "
			+ "ti.sampleWeight, ti.precentage, "
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
			+ "join qc.processType pt "
			+ "join qc.lifeCycle lc "
		+ "where (qc.id in :processIds or po_code.id in :poCodeIds) "
			+ "and (qc.checkedBy = :checkedBy or :checkedBy is null) "
			+ "and (i.productionUse = :productionUse or :productionUse is null) "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
		+ "order by qc.recordedTime ASC , ti.ordinal ")
	Stream<ItemQc> findCashewQcItems(int[] processIds, int[] poCodeIds, QcCompany checkedBy, ProductionUse productionUse, boolean cancelled);

	/**
	 * Gets loading summary for final report by 'PO code' (also if product is from a combination of other POs besides the given one). 
	 * @param poCodeId id of 'PO code' for whom to get the loading data summary.
	 * @param cancelled, if to include cancelled loadings in summary.
	 * @return List of ItemAmountWithLoadingReportLine which is pair of LoadingReportLine and ItemAmount for one item.
	 */
	@Query("select new com.avc.mis.beta.dto.query.ItemAmountWithLoadingReportLine("
			+ "p.id, sc.id, sc.code, port.code, port.value, cont_arrival.containerDetails, p.recordedTime, "
			+ "lc.processStatus, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ': ', approval.decision))), "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((sf.numberUnits * coalesce(sf.unitAmount, 1)) * uom.multiplicand / uom.divisor), "
			+ "coalesce(w_po_used_item.weight, 1)) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode sc "
				+ "join sc.portOfDischarge port "
			+ "join p.arrival cont_arrival "
			+ "join p.storageMovesGroups grp "
				+ "join grp.storageMoves sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
								+ "join item.unit item_unit "
							+ "join pi.process p_used_item "
								+ "left join p_used_item.poCode po_code_used_item "
								+ "left join p_used_item.weightedPos w_po_used_item "
									+ "left join w_po_used_item.poCode w_po_code_used_item "
								+ "join PoCode po_code "
									+ "on (po_code = po_code_used_item or po_code = w_po_code_used_item) "
						+ "join UOM uom "
							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
			+ "join p.lifeCycle lc "
			+ "left join p.approvals approval "
				+ "left join approval.user u "
		+ "where "
			+ "po_code.id = :poCodeId "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
		+ "group by p, item, w_po_code_used_item ")
	List<ItemAmountWithLoadingReportLine> findLoadingsItemsAmounts(Integer poCodeId, boolean cancelled);

}
