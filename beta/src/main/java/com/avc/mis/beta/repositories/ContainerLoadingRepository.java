/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.ContainerLoadingDTO;
import com.avc.mis.beta.dto.query.LoadedItemWithStorage;
import com.avc.mis.beta.dto.query.UsedItemWithGroup;
import com.avc.mis.beta.dto.view.LoadingRow;
import com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount;
import com.avc.mis.beta.entities.process.ContainerLoading;

/**
 * @author zvi
 *
 */
public interface ContainerLoadingRepository  extends ProcessRepository<ContainerLoading> {

	@Query("select new com.avc.mis.beta.dto.process.ContainerLoadingDTO("
			+ "r.id, r.version, r.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "r.recordedTime, r.duration, r.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, r.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision))) "
		+ "from ContainerLoading r "
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
	@Query("select new com.avc.mis.beta.dto.query.LoadedItemWithStorage( "
			+ " i.id, i.version, "
			+ "item.id, item.value, item.category, "
			+ "poCode.code, ct.code, ct.suffix, s.name, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "unit.amount, unit.measureUnit, sf.numberUnits, sf.containerWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "i.description, i.remarks, i.tableView, "
			+ "itemPoCode.code, ctItem.code, ctItem.suffix, sItem.name) "
		+ "from LoadedItem i "
			+ "join i.item item "
			+ "join i.process p "
				+ "join p.poCode poCode "
					+ "join poCode.contractType ct "
					+ "join poCode.supplier s "
			+ "join i.storageForms sf "
				+ "join sf.unitAmount unit "
				+ "left join sf.warehouseLocation warehouseLocation "
			+ "join i.poCode itemPoCode "
				+ "join itemPoCode.contractType ctItem "
				+ "join itemPoCode.supplier sItem "
		+ "where p.id = :processId ")
	List<LoadedItemWithStorage> findLoadedItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((unit.amount * ui.numberUnits - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit) "
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
//			+ "join p.processType pt "
//		+ "where pt.processName = :processName "
//			+ "and po_code_used_item.code = po_code.code "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllUsedItems();

	@Query("select new com.avc.mis.beta.dto.view.ProductionProcessWithItemAmount("
			+ "p.id, item.id, item.value, "
			+ "SUM((unit.amount * sf.numberUnits - coalesce(sf.containerWeight, 0)) * uom.multiplicand / uom.divisor), "
			+ "item.measureUnit) "
		+ "from ContainerLoading p "
			+ "join p.processItems pi "
				+ "join pi.item item "
				+ "join pi.storageForms sf "
					+ "join sf.unitAmount unit "
					+ "join UOM uom "
						+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = item.measureUnit "
//			+ "join p.processType pt "
//		+ "where pt.processName = :processName "
		+ "group by p, item ")
	Stream<ProductionProcessWithItemAmount> findAllLoadedItems();

	@Query("select new com.avc.mis.beta.dto.view.LoadingRow( "
			+ "p.id, shipment_code.code, pod.code, pod.value, "
			+ "p.recordedTime, p.duration) "
		+ "from ContainerLoading p "
			+ "join p.shipmentCode shipment_code "
				+ "join shipment_code.portOfDischarge pod ")
	List<LoadingRow> findContainerLoadings();

}
