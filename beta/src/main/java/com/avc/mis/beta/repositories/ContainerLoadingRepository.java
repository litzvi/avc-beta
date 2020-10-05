/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.doc.ContainerPoItemRow;
import com.avc.mis.beta.dto.doc.ContainerPoItemStorageRow;
import com.avc.mis.beta.dto.doc.ExportInfo;
import com.avc.mis.beta.dto.doc.InventoryExportDoc;
import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.embeddable.ShipingDetails;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.ContainerLoading;

import lombok.NonNull;

/**
 * @author zvi
 *
 */
public interface ContainerLoadingRepository  extends TransactionProcessRepository<ContainerLoading> {

	@Query("select new com.avc.mis.beta.dto.process.ContainerLoadingDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.startTime, r.endTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
			+ "sc.code, port.id, port.code, port.value, "
			+ "r.containerDetails, r.shipingDetails) "
		+ "from ContainerLoading r "
			+ "join r.shipmentCode sc "
				+ "join sc.portOfDischarge port "
			+ "left join r.poCode po_code "
				+ "left join po_code.contractType t "
				+ "left join po_code.supplier s "
			+ "join r.processType pt "
			+ "left join r.createdBy p_user "
			+ "left join r.productionLine p_line "
			+ "join r.lifeCycle lc "
			+ "left join r.approvals approval "
				+ "left join approval.user u "
		+ "where r.id = :processId ")
	Optional<ContainerLoadingDTO> findContainerLoadingDTOById(int processId);

	/**
	 * Gets the join of loaded item, process and storage information for the given container loading process.
	 * @param processId id of the process
	 * @return List of LoadedItemWithStorage
	 */
	@Query("select new com.avc.mis.beta.dto.processinfo.LoadedItemDTO( "
			+ " i.id, i.version, i.ordinal, "
			+ "item.id, item.value, item.category, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
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


	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((unit.amount * ui.numberUsedUnits - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', coalesce(wh.value, ''))) "
		+ "from ContainerLoading p "
//			+ "left join p.poCode po_code "
			+ "join p.usedItemGroups grp "
				+ "join grp.usedItems ui "
					+ "join ui.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.item item "
//								+ "join pi.process p_used_item "
//									+ "join p_used_item.poCode po_code_used_item "
						+ "join sf.unitAmount unit "
						+ "join UOM uom "
							+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
						+ "left join sf.warehouseLocation wh "
//			+ "join p.processType pt "
//		+ "where pt.processName = :processName "
//			+ "and po_code_used_item.code = po_code.code "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItems();

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((unit.amount * sf.numberUnits - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit, function('GROUP_CONCAT', coalesce(wh.value, ''))) "
		+ "from ContainerLoading p "
			+ "join p.processItems pi "
				+ "join pi.item item "
				+ "join pi.storageForms sf "
					+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
					+ "left join sf.warehouseLocation wh "
//			+ "join p.processType pt "
//		+ "where pt.processName = :processName "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllLoadedItems();

	@Query("select new com.avc.mis.beta.dto.view.LoadingRow( "
			+ "p.id, shipment_code.code, pod.code, pod.value, "
			+ "p.recordedTime, p.duration, ship.eta, cont.containerType) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode shipment_code "
				+ "join shipment_code.portOfDischarge pod "
		+ "join p.containerDetails cont "
		+ "join p.shipingDetails ship ")
	List<LoadingRow> findContainerLoadings();

	@Query("select new com.avc.mis.beta.dto.doc.ExportInfo( "
			+ "shipment_code.id, pod.code, pod.value, p.recordedTime) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode shipment_code "
				+ "join shipment_code.portOfDischarge pod "
		+ "where p.id = :processId ")
	Optional<ExportInfo> findInventoryExportDocById(int processId);

	@Query("select new com.avc.mis.beta.dto.doc.ContainerPoItemRow( "
			+ "item.value, itemPo.code, ct.code, ct.suffix, "
			+ "sum((unit.amount - coalesce(sf.containerWeight, 0)) * i.numberUsedUnits * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit) "
		+ "from ContainerLoading p "
			+ "join p.usedItemGroups g "
				+ "join g.usedItems i "
					+ "join i.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.process used_p "
								+ "join used_p.poCode itemPo "
									+ "left join itemPo.contractType ct "
							+ "join pi.item item "
						+ "join sf.unitAmount unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "				
		+ "where p.id = :processId "
		+ "group by item, itemPo.code ")
	List<ContainerPoItemRow> findLoadedTotals(int processId);

	@Query("select new com.avc.mis.beta.dto.doc.ContainerPoItemStorageRow( "
			+ "item.value, itemPo.code, ct.code, ct.suffix, "
			+ "unit.amount, unit.measureUnit, sum(i.numberUsedUnits)) "
		+ "from ContainerLoading p "
			+ "join p.usedItemGroups g "
				+ "join g.usedItems i "
					+ "join i.storage sf "
						+ "join sf.processItem pi "
							+ "join pi.process used_p "
								+ "join used_p.poCode itemPo "
									+ "left join itemPo.contractType ct "
							+ "join pi.item item "
						+ "join sf.unitAmount unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "				
		+ "where p.id = :processId "
		+ "group by item, itemPo.code, sf ")
	List<ContainerPoItemStorageRow> findLoadedStorages(int processId);

	
}
