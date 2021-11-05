/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.query.ReceiptItemWithStorage;
import com.avc.mis.beta.dto.view.ReceiptItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.Receipt;

/**
 * @author Zvi
 *
 */
public interface ReceiptRepository extends ProcessWithProductRepository<Receipt> {
	
	@Query("select new com.avc.mis.beta.dto.query.ReceiptItemWithStorage( "
			+ " i.id, i.version, i.ordinal, item.id, item.value, item.productionUse, "
			+ "item_unit, type(item), sf_group.measureUnit, "
			+ "sf.id, sf.version, sf.ordinal, "
			+ "sf.unitAmount, sf.numberUnits, "
//			+ "sf.accessWeight, "
			+ "warehouseLocation.id, warehouseLocation.value, sf.remarks, type(sf), "
			+ "sf.sampleContainerWeights, sf.sampleWeights, sf.numberOfSamples, sf.avgTestedWeight, "
			+ "i.groupName, i.description, i.remarks, i.tableView, "
			+ "ru.amount, ru.measureUnit, up.amount, up.currency, "
			+ "po.id, oi.id, oi.version, extra.amount, extra.measureUnit) "
		+ "from ReceiptItem i "
			+ "left join i.orderItem oi "
				+ "left join oi.po po "
			+ "join i.item item "
				+ "join item.unit item_unit "
			+ "left join i.receivedOrderUnits ru "
			+ "left join i.unitPrice up "
			+ "join i.process p "
			+ "join i.storageForms sf "
				+ "join sf.group sf_group "
				+ "left join sf.warehouseLocation warehouseLocation "
			+ "left join i.extraRequested extra "
		+ "where p.id = :processId "
		+ "order by i.ordinal, sf.ordinal, sf.dtype "
//		+ ", sf.ordinal " //already sorted in dto for storages
		+ "")
	List<ReceiptItemWithStorage> findReceiptItemWithStorage(int processId);

	@Query("select new com.avc.mis.beta.dto.view.ReceiptItemRow( "
				+ "r.id, po_code.id, po_code.code, ct.code, ct.suffix, s.name,  "
				+ "item.id, item.value, item.measureUnit, item.itemGroup, item_unit, type(item), "
				+ "units.amount, units.measureUnit, "
				+ "ro_units.amount, ro_units.measureUnit, "
				+ "r.recordedTime, lc.processStatus, "
				+ "SUM(coalesce(sf.unitAmount, 1) * sf.numberUnits * uom.multiplicand / uom.divisor), item.measureUnit, "
				+ "function('GROUP_CONCAT', function('DISTINCT', sto.value)), "
				+ "extra.amount, extra.measureUnit) "
			+ "from Receipt r "
				+ "join r.lifeCycle lc "
				+ "join r.poCode po_code "
					+ "join po_code.supplier s "
					+ "join po_code.contractType ct "
				+ "join r.processItems pi "
					+ "left join pi.extraRequested extra "
					+ "join pi.item item "
						+ "join item.unit item_unit "
					+ "join pi.storageForms sf "
						+ "left join sf.warehouseLocation sto "
//						+ "join sf.group sf_group "
				+ "join UOM uom "
					+ "on uom.fromUnit = pi.measureUnit and uom.toUnit = item.measureUnit "
//					+ "left join ExtraAdded as added "
//						+ "on added.processItem = pi "
					+ "left join pi.orderItem oi "
						+ "left join oi.numberUnits units "
					+ "join pi.receivedOrderUnits ro_units "
//					+ "left join pi.extraRequested extra "
				+ "join r.processType t "
//			+ "where type(sf) <> ExtraAdded "
			+ "where t.processName in :processNames "
				+ "and lc.processStatus in :statuses "
				+ "and (po_code.id = :poCodeId or :poCodeId is null)"
				+ "and (:startTime is null or r.recordedTime >= :startTime) "
				+ "and (:endTime is null or r.recordedTime < :endTime) "
			+ "group by r.id, oi, pi "
			+ "order by r.recordedTime desc ")
	List<ReceiptItemRow> findAllReceiptsByType(
			ProcessName[] processNames, ProcessStatus[] statuses, Integer poCodeId, 
			LocalDateTime startTime, LocalDateTime endTime);

	@Query("select e.id "
			+ "from ReceiptItem ri "
				+ "join ExtraAdded e "
					+ "on e.processItem.id = ri.id "
			+ "where ri.id = :receiptItemId ")
	List<Integer> findAddedByReceiptItem(Integer receiptItemId);

}
