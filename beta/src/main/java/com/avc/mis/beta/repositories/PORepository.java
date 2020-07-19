/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.processinfo.OrderItemDTO;
import com.avc.mis.beta.dto.report.PoItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PO;

/**
 * Spring repository for accessing purchase order information.
 * 
 * @author Zvi
 *
 */
public interface PORepository extends BaseRepository<PO> {
	
	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "po.recordedTime, po.duration, po.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, po.remarks, po.personInCharge) "
		+ "from PO po "
			+ "join po.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join po.processType pt "
			+ "left join po.createdBy p_user "
			+ "left join po.productionLine p_line "
			+ "join po.lifeCycle lc "
		+ "where po_code.id = :codeId ")
	Optional<PoDTO> findOrderByPoCodeId(Integer codeId);
	
	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "po.recordedTime, po.duration, po.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, po.remarks, po.personInCharge) "
		+ "from PO po "
			+ "join po.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join po.processType pt "
			+ "left join po.createdBy p_user "
			+ "left join po.productionLine p_line "
			+ "join po.lifeCycle lc "
		+ "where po.id = :id ")
	Optional<PoDTO> findOrderByProcessId(Integer id);
	
	@Query("select new com.avc.mis.beta.dto.processinfo.OrderItemDTO("
			+ "i.id, i.version, item.id, item.value, units.amount, units.measureUnit, "
			+ "price.amount, price.currency, i.deliveryDate, i.defects, i.remarks, "
			+ "SUM(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) ) "
//			+ "ri is not null) "
		+ "from OrderItem i "
			+ "join i.numberUnits units "
				+ "left join i.receiptItems ri "
					+ "left join ri.process r "
						+ "left join r.lifeCycle rlc "		
					+ "left join ri.storageForms sf "
						+ "left join sf.unitAmount unit "
							+ "left join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = units.measureUnit "
//				+ "left join ReceiptItem ri on ri.orderItem = i "
			+ "left join i.unitPrice price "
			+ "join i.item item "
			+ "join i.po po "
		+ "where po.id = :poid and "
			+ "(rlc is null or rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
			+ "group by i ")
	Set<OrderItemDTO> findOrderItemsByPo(Integer poid);
	
	@Query("select new com.avc.mis.beta.dto.report.PoItemRow(po.id, po.personInCharge, po_code.code, ct.code, ct.suffix, s.name, i.value, "
			+ "units.amount, units.measureUnit, po.recordedTime, oi.deliveryDate, "
			+ "oi.defects, price.amount, price.currency, 'PENDING') "
		+ "from PO po "
		+ "join po.lifeCycle lc "
		+ "join po.poCode po_code "
			+ "join po_code.contractType ct "
			+ "join po_code.supplier s "
		+ "join po.processType t "
		+ "join po.orderItems oi "
			+ "join oi.numberUnits units "
			+ "left join oi.unitPrice price "
			+ "join oi.item i "
			//check for sum
			+ "where not exists (select ri from ReceiptItem ri "
								+ "join ri.process r "
									+ "join r.lifeCycle rlc "
							+ "where ri.orderItem = oi and "
								+ "rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
			+ "and t.processName = ?1 "
			+ "and lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED")
//		+ "ORDER BY oi.deliveryDate DESC ") - done in the java code
	List<PoItemRow> findOpenOrdersByType(ProcessName orderType);

	@Query("select new com.avc.mis.beta.dto.report.PoItemRow(po.id, po.personInCharge, po_code.code, ct.code, ct.suffix, s.name, i.value, "
			+ "units.amount, units.measureUnit, po.recordedTime, oi.deliveryDate, "
			+ "oi.defects, price.amount, price.currency, "
			+ "CASE WHEN lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN 'CANCELLED' "
				+ "WHEN oi.receiptItems is empty THEN 'PENDING' "
				+ "WHEN ri is null THEN 'REJECTED' "
				+ "WHEN SUM(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) < units.amount THEN 'PARTLY RECEIVED' "
				+ "ELSE 'RECEIVED'"
				+ "END) "
		+ "from PO po "
		+ "join po.lifeCycle lc "
		+ "join po.poCode po_code "
			+ "join po_code.contractType ct "
			+ "join po_code.supplier s "
		+ "join po.processType t "
		+ "join po.orderItems oi "
			+ "join oi.numberUnits units "
			+ "left join oi.unitPrice price "
			+ "join oi.item i "
			+ "left join oi.receiptItems ri "
				+ "left join ri.process r "
					+ "left join r.lifeCycle rlc "		
				+ "left join ri.storageForms sf "
					+ "left join sf.unitAmount unit "
						+ "left join UOM uom "
							+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = units.measureUnit "
		+ "where t.processName = ?1 and "
			+ "(rlc is null or rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
		+ "group by oi ")
//			+ "and lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED")
	List<PoItemRow> findAllOrdersByType(ProcessName orderType);

	
//	@Query("select new com.avc.mis.beta.dto.values.PoBasic(po.id, po_code, s.name, po.orderStatus) "
//		+ "from PO po "
//		+ "left join po.poCode po_code "
//		+ "left join po.supplier s "
//		+ "left join po.processType t "
//		+ "where t.processName = ?1 and po.orderStatus in ?2 ")
//	List<PoBasic> findByOrderTypeAndStatusesBasic(ProcessName orderType, OrderStatus[] statuses);
//	
//	@Query("select new com.avc.mis.beta.dto.values.PoBasic(po.id, po_code, s.name, po.orderStatus) "
//		+ "from PO po "
//		+ "left join po.poCode po_code "
//		+ "left join po.supplier s "
//		+ "left join po.processType t "
//		+ "left join po.orderItems oi "
//		+ "where t.processName = ?1 and oi.status in ?2 "
//		+ "group by po.id ")
//	List<PoBasic> findByOrderTypeAndItemStatusesBasic(ProcessName orderType, OrderItemStatus[] statuses);
//		
//	
//	@Query("select new com.avc.mis.beta.dto.values.PoRow(po.id, po_code, s.name, i.value, "
//			+ "oi.numberUnits, oi.measureUnit, po.recordedTime, oi.deliveryDate, po.orderStatus, "
//			+ "oi.defects, oi.currency, oi.unitPrice) "
//		+ "from PO po "
//		+ "left join po.poCode po_code "
//		+ "left join po.supplier s "
//		+ "left join po.processType t "
//		+ "join po.orderItems oi "
//			+ "left join oi.item i "
//		+ "where t.processName = ?1 and oi.status in ?2 ")
////		+ "ORDER BY po.createdDate DESC ") // maybe by delivery date
//	List<PoRow> findByOrderTypeAndItemStatuses(ProcessName orderType, OrderItemStatus[] statuses);
//	
//	@Query("select new com.avc.mis.beta.dto.values.PoRow(po.id, po_code, s.name, i.value, "
//			+ "oi.numberUnits, oi.measureUnit, po.recordedTime, oi.deliveryDate, po.orderStatus, "
//			+ "oi.defects, oi.currency, oi.unitPrice) "
//		+ "from PO po "
//		+ "left join po.poCode po_code "
//		+ "left join po.supplier s "
//		+ "left join po.processType t "
//		+ "join po.orderItems oi "
//			+ "left join oi.item i "
//		+ "where t.processName = ?1 and po.orderStatus in ?2 ")
////		+ "ORDER BY po.createdDate DESC ") // maybe by delivery date
//	List<PoRow> findByOrderTypeAndStatuses(ProcessName orderType, OrderStatus[] statuses);

	
}
