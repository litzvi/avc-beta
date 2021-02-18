/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.doc.ContainerPoItemRow;
import com.avc.mis.beta.dto.doc.ContainerPoItemStorageRow;
import com.avc.mis.beta.dto.doc.ExportInfo;
import com.avc.mis.beta.dto.embedable.ContainerLoadingInfo;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.query.ItemAmountWithLoadingReportLine;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.ContainerLoading;

/**
 * @author zvi
 *
 */
public interface ContainerLoadingRepository  extends TransactionProcessRepository<ContainerLoading> {
	
	/**
	 * Gets loading summary for final report by 'PO code' (also if product is from a combination of other POs besides the given one). 
	 * @param poCodeId id of 'PO code' for whom to get the loading data summary.
	 * @param cancelled, if to include cancelled loadings in summary.
	 * @return List of ItemAmountWithLoadingReportLine which is pair of LoadingReportLine and ItemAmount for one item.
	 */
	@Query("select new com.avc.mis.beta.dto.query.ItemAmountWithLoadingReportLine("
			+ "p.id, sc.id, sc.code, port.code, port.value, p.containerDetails, p.recordedTime, "
			+ "lc.processStatus, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "SUM((ui.numberUnits * sf.unitAmount - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "coalesce(w_po_used_item.weight, 1)) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode sc "
				+ "join sc.portOfDischarge port "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
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

	/**
	 * Gets the access loading information (over the general process information assumed to be fetched).
	 * @param processId id of loading process info to be fetched.
	 * @return ContainerLoadingInfo object that contains loading process information.
	 */
	@Query("select new com.avc.mis.beta.dto.embedable.ContainerLoadingInfo( "
			+ "sc.id, sc.code, port.id, port.value, port.code, "
			+ "r.containerDetails, r.shipingDetails) "
		+ "from ContainerLoading r "
			+ "join r.shipmentCode sc "
				+ "join sc.portOfDischarge port "
		+ "where r.id = :processId ")
	ContainerLoadingInfo findContainerLoadingInfo(int processId);

	/**
	 * Gets the join of loaded item, process and storage information for the given container loading process.
	 * @param processId id of the process
	 * @return List of LoadedItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.processinfo.LoadedItemDTO( "
			+ " i.id, i.version, i.ordinal, "
			+ "item.id, item.value, item.productionUse, type(item), "
			+ "poCode.id, poCode.code, ct.code, ct.suffix, s.name, poCode.display, "
			+ "da.amount, da.measureUnit, "
			+ "i.description, i.remarks) "
		+ "from LoadedItem i "
			+ "join i.item item "
			+ "left join i.declaredAmount da "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
		+ "where p.id = :processId "
		+ "order by i.ordinal ")
	List<LoadedItemDTO> findLoadedItems(int processId);


//	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
//			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "SUM((sf.unitAmount * ui.numberUnits - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
//			+ "function('GROUP_CONCAT', coalesce(wh.value, ''))) "
//		+ "from ContainerLoading p "
////			+ "left join p.poCode po_code "
//			+ "join p.usedItemGroups grp "
//				+ "join grp.usedItems ui "
//					+ "join ui.storage sf "
//						+ "join sf.processItem pi "
//							+ "join pi.item item "
//								+ "join item.unit item_unit "
////								+ "join pi.process p_used_item "
////									+ "join p_used_item.poCode po_code_used_item "
////						+ "join sf.unitAmount unit "
//						+ "join UOM uom "
//							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
//						+ "left join sf.warehouseLocation wh "
////			+ "join p.processType pt "
////		+ "where pt.processName = :processName "
////			+ "and po_code_used_item.code = po_code.code "
//		+ "group by p, item ")
//	Stream<ProductionProcessWithItemAmount> findAllUsedItems();
	
//	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
//			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "SUM((sf.unitAmount * ui.numberUnits - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
//			+ "function('GROUP_CONCAT', coalesce(wh.value, ''))) "
//		+ "from ContainerLoading p "
////			+ "left join p.poCode po_code "
//			+ "join p.usedItemGroups grp "
//				+ "join grp.usedItems ui "
//					+ "join ui.storage sf "
//						+ "join sf.processItem pi "
//							+ "join pi.item item "
//								+ "join item.unit item_unit "
//							+ "join pi.process p_used_item "
//								+ "left join p_used_item.poCode po_code_used_item "
//								+ "left join p_used_item.weightedPos w_po "
//								+ "left join w_po.poCode w_po_code "
////						+ "join sf.unitAmount unit "
//						+ "join UOM uom "
//							+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
//						+ "left join sf.warehouseLocation wh "
//		+ "where "
//			+ "(coalesce(po_code_used_item.id, null) = :poCodeId) "
//			+ "or (coalesce(w_po_code.id, null) = :poCodeId) "
//		+ "group by p, item ")
//	Stream<ProductionProcessWithItemAmount> findAllUsedItemsByPoCode(Integer poCodeId);


//	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
//			+ "p.id, item.id, item.value, item.measureUnit, item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "SUM((sf.unitAmount * sf.numberUnits - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
//			+ "function('GROUP_CONCAT', coalesce(wh.value, ''))) "
//		+ "from ContainerLoading p "
//			+ "join p.processItems pi "
//				+ "join pi.item item "
//					+ "join item.unit item_unit "
//				+ "join pi.storageForms sf "
////					+ "join sf.unitAmount unit "
//					+ "join UOM uom "
//						+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
//					+ "left join sf.warehouseLocation wh "
////			+ "join p.processType pt "
////		+ "where pt.processName = :processName "
//		+ "group by p, item ")
//	Stream<ProductionProcessWithItemAmount> findAllLoadedItems();

	@Query("select new com.avc.mis.beta.dto.view.LoadingRow( "
			+ "p.id, "
			+ "function('GROUP_CONCAT', po_code.id), "
			+ "function('GROUP_CONCAT', concat(t.code, '-', po_code.code, t.suffix)), "
			+ "function('GROUP_CONCAT', s.name), "
			+ "p.recordedTime, p.duration, lc.processStatus, "
			+ "function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
			+ "shipment_code.id, shipment_code.code, pod.code, pod.value, "
			+ "ship.eta, cont.containerNumber, cont.sealNumber, cont.containerType) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode shipment_code "
				+ "join shipment_code.portOfDischarge pod "
			+ "join p.containerDetails cont "
			+ "join p.shipingDetails ship "
			+ "join p.processType pt "
			+ "join p.lifeCycle lc "
			+ "left join p.approvals approval "
				+ "left join approval.user u "
 			+ "join p.usedItemGroups grp "
			+ "join grp.usedItems ui "
				+ "join ui.storage sf "
					+ "join sf.processItem pi "
						+ "join pi.process p_used_item "
							+ "left join p_used_item.poCode po_code_used_item "
							+ "left join p_used_item.weightedPos w_po "
								+ "left join w_po.poCode w_po_code "
							+ "join PoCode po_code "
								+ "on (po_code = po_code_used_item or po_code = w_po_code) "
								+ "join po_code.contractType t "
								+ "join po_code.supplier s "
		+ "where "
			+ "(po_code.id = :poCodeId or :poCodeId is null) "
			+ "and ((:cancelled is true) or (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED)) "
		+ "group by p "
		+ "order by p.recordedTime desc ")
	List<LoadingRow> findContainerLoadings(Integer poCodeId, boolean cancelled);
	
//	@Query("select new com.avc.mis.beta.dto.view.LoadingRow( "
//			+ "p.id, shipment_code.id, shipment_code.code, pod.code, pod.value, "
//			+ "p.recordedTime, p.duration, lc.processStatus, ship.eta, "
//			+ "cont.containerNumber, cont.sealNumber, cont.containerType) "
//		+ "from ContainerLoading p "
//			+ "join p.shipmentCode shipment_code "
//				+ "join shipment_code.portOfDischarge pod "
//			+ "join p.containerDetails cont "
//			+ "join p.shipingDetails ship "
//			+ "join p.lifeCycle lc "
//		+ "where "
//			+ "p.id in :processIds ")
//	List<LoadingRow> findContainerLoadingsByProcessIds(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.doc.ExportInfo( "
			+ "shipment_code.id, shipment_code.code, pod.code, pod.value, p.recordedTime) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode shipment_code "
				+ "join shipment_code.portOfDischarge pod "
		+ "where p.id = :processId ")
	Optional<ExportInfo> findInventoryExportDocById(int processId);

	@Query("select new com.avc.mis.beta.dto.doc.ContainerPoItemRow( "
			+ "p.id, "
			+ "item.id, item.value, item.measureUnit, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "item_po_code.id, item_po_code.code, t.code, t.suffix, s.name, item_po_code.display, "
			+ "CASE "
				+ "WHEN item_po_code is null "
					+ "THEN function('GROUP_CONCAT', concat(w_t.code, '-', w_po_code.code, w_t.suffix)) "
				+ "ELSE function('GROUP_CONCAT', concat(t.code, '-', item_po_code.code, t.suffix)) "
			+ "END, "
			+ "sum((sf.unitAmount * i.numberUnits - coalesce(sf.accessWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit) "
		+ "from ContainerLoading p "
			+ "join p.usedItemGroups g "
				+ "join g.usedItems i "
					+ "join i.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.process used_p "
								+ "left join used_p.poCode item_po_code "
									+ "left join item_po_code.contractType t "
									+ "left join item_po_code.supplier s "
								+ "left join used_p.weightedPos w_item_po "
									+ "left join w_item_po.poCode w_po_code "
										+ "left join w_po_code.contractType w_t "
										+ "left join w_po_code.supplier w_s "
							+ "join pi.item item "
								+ "join item.unit item_unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "				
		+ "where p.id in :processIds  "
		+ "group by p, item.id, item_po_code.id, w_po_code.id ")
	List<ContainerPoItemRow> findLoadedTotals(int[] processIds);

	@Query("select new com.avc.mis.beta.dto.doc.ContainerPoItemStorageRow( "
			+ "item.id, item.value, item.measureUnit, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
			+ "itemPo.id, itemPo.code, ct.code, ct.suffix, s.name, itemPo.display, "
			+ "sf.unitAmount, pi.measureUnit, sum(i.numberUnits)) "
		+ "from ContainerLoading p "
			+ "join p.usedItemGroups g "
				+ "join g.usedItems i "
					+ "join i.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.process used_p "
								+ "left join used_p.poCode itemPo "
									+ "left join itemPo.contractType ct "
									+ "left join itemPo.supplier s "
							+ "join pi.item item "
								+ "join item.unit item_unit "
//						+ "join sf.unitAmount unit "
//							+ "join UOM uom "
//								+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.defaultMeasureUnit "				
		+ "where p.id = :processId "
		+ "group by item.id, itemPo.id, sf.unitAmount, pi.measureUnit ")
	List<ContainerPoItemStorageRow> findLoadedStorages(int processId);





	
}
