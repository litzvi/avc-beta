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
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.processinfo.OrderItem;

/**
 * Spring repository for accessing purchase order information.
 * 
 * @author Zvi
 *
 */
public interface PORepository extends BaseRepository<PO> {
	
	/**
	 * Gets the PO details in a PoDTO object by process id (exclusive) or po code id
	 * @param processId the process id of the PO
	 * @param poCodeId po code id of the PO
	 * @return PoDTO a DTO of a PO with all process information.
	 */
	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user.username, "
			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "po.recordedTime, po.startTime, po.endTime, po.duration, po.numOfWorkers, "
			+ "lc.processStatus, lc.editStatus, po.remarks, function('GROUP_CONCAT', concat(u.username, ':', approval.decision)), "
			+ "po.personInCharge) "
		+ "from PO po "
			+ "join po.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join po.processType pt "
			+ "left join po.createdBy p_user "
			+ "left join po.productionLine p_line "
			+ "join po.lifeCycle lc "
			+ "left join po.approvals approval "
				+ "left join approval.user u "
		+ "where po.id = :processId or po_code.id = :poCodeId "
			+ "and (:processId is null or :poCodeId is null)")
	Optional<PoDTO> findOrderById(Integer processId, Integer poCodeId);
	
	/**
	 * Gets all OrderItems for a given process in a OrderItemDTO that contains order 
	 * information and amount received.
	 * @param processId the process id of the PO
	 * @return Set of OrderItemDTOs for the given process
	 */
	@Query("select new com.avc.mis.beta.dto.processinfo.OrderItemDTO("
			+ "i.id, i.version, i.ordinal, item.id, item.value, units.amount, units.measureUnit, "
			+ "price.amount, price.currency, i.deliveryDate, i.defects, i.remarks, "
			+ "SUM( "
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
					+ "ELSE (unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) "
				+ "END) ) "
		+ "from OrderItem i "
			+ "join i.po po "
			+ "join i.numberUnits units "
				+ "left join i.receiptItems ri "
					+ "left join ri.process r "
						+ "left join r.lifeCycle rlc "		
					+ "left join ri.storageForms sf "
						+ "left join sf.unitAmount unit "
							+ "left join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = units.measureUnit "
			+ "left join i.unitPrice price "
			+ "join i.item item "
		+ "where po.id = :processId "
		+ "group by i "
		+ "order by i.ordinal ")
	List<OrderItemDTO> findPoOrderItemsById(Integer processId);
	
	/**
	 * Gets rows of open orders for the given order type - 
	 * orders that aren't cancelled and not fully received.
	 * @param orderType e.g. GENERAL_ORDER, CASHEW_ORDER
	 * @return List of PoItemRow for all open orders sorted by delivery date.
	 */
	@Query("select new com.avc.mis.beta.dto.view.PoItemRow(po.id, po.personInCharge, po_code.code, ct.code, ct.suffix, s.name, "
			+ "function('GROUP_CONCAT', concat(coalesce(user.username, ''), ' - ', coalesce(approval.decision, ''))), "
			+ "i.value, units.amount, units.measureUnit, po.recordedTime, oi.deliveryDate, "
			+ "oi.defects, price.amount, price.currency, "
			+ "SUM( "
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
					+ "ELSE (unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) "
				+ "END "
			+ "), "
			+ "lc.processStatus, "
			+ "SUM( "
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN 1 "
					+ "ELSE 0 "
				+ "END "
			+ ") "
		+ ") "
		+ "from PO po "
			+ "join po.lifeCycle lc "
			+ "join po.poCode po_code "
				+ "join po_code.contractType ct "
				+ "join po_code.supplier s "
			+ "join po.processType t "
			+ "left join po.approvals approval "
				+ "left join approval.user user "
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
			+ "where "
				+ "t.processName = ?1 "
				+ "and (lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.PENDING "
					+ "or lc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
			+ "group by oi "
			+ "having coalesce("
				+ "SUM("
					+ "CASE "
						+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
						+ "ELSE (unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) "
					+ "END), "
				+ "0) < units.amount "
			+ "ORDER BY oi.deliveryDate, po.id ") // done in the java code if aggregated
	List<PoItemRow> findOpenOrdersByType(ProcessName orderType);

	/**
	 * Gets rows of all orders (history) for the given order type with their order status. 
	 * @param orderType e.g. GENERAL_ORDER, CASHEW_ORDER
	 * @return List of PoItemRow for all orders.
	 */
		@Query("select new com.avc.mis.beta.dto.view.PoItemRow(po.id, po.personInCharge, po_code.code, ct.code, ct.suffix, s.name, "
				+ "function('GROUP_CONCAT', concat(coalesce(user.username, ''), ':', coalesce(approval.decision, ''))), "
				+ "i.value, units.amount, units.measureUnit, po.recordedTime, oi.deliveryDate, "
				+ "oi.defects, price.amount, price.currency, "
				+ "SUM( "
					+ "CASE "
						+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
						+ "ELSE (unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) "
					+ "END "
				+ "), "
				+ "lc.processStatus, "
				+ "SUM( "
					+ "CASE "
						+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN 1 "
						+ "ELSE 0 "
					+ "END "
				+ ") "
			+ ") "
			+ "from PO po "
				+ "join po.lifeCycle lc "
				+ "join po.poCode po_code "
					+ "join po_code.contractType ct "
					+ "join po_code.supplier s "
				+ "join po.processType t "
				+ "left join po.approvals approval "
					+ "left join approval.user user "
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
			+ "where "
				+ "t.processName = ?1 "
				+ "and lc.processStatus in ?2 "
			+ "group by oi "
			+ "ORDER BY oi.deliveryDate, po.id ")
		List<PoItemRow> findAllOrdersByType(ProcessName orderType, ProcessStatus[] processStatuses);

	@Query("select oi "
			+ "from OrderItem oi "
				+ "join oi.numberUnits nu "
				+ "join oi.receiptItems ri "
					+ "join ri.process r "
						+ "join r.lifeCycle rlc "		
					+ "join ri.storageForms sf "
						+ "join sf.unitAmount unit "
							+ "join UOM uom "
								+ "on uom.fromUnit = unit.measureUnit and uom.toUnit = nu.measureUnit "
			+ "where oi.id in :orderItemIds "
				+ "and rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
			+ "group by oi "
			+ "having nu.amount <= sum(unit.amount * sf.numberUnits * uom.multiplicand / uom.divisor) ")
	List<OrderItem> findNonOpenOrderItemsById(Integer[] orderItemIds);


//	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
//			+ "po.id, po.version, po.createdDate, p_user.username, "
//			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
//			+ "pt.processName, p_line, "
//			+ "po.recordedTime, po.duration, po.numOfWorkers, "
//			+ "lc.processStatus, lc.editStatus, po.remarks, po.personInCharge) "
//		+ "from PO po "
//			+ "join po.poCode po_code "
//				+ "join po_code.contractType t "
//				+ "join po_code.supplier s "
//			+ "join po.processType pt "
//			+ "left join po.createdBy p_user "
//			+ "left join po.productionLine p_line "
//			+ "join po.lifeCycle lc "
//		+ "where po_code.id = :codeId ")
//	Optional<PoDTO> findOrderByPoCodeId(Integer codeId);
//	
//	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
//			+ "po.id, po.version, po.createdDate, p_user.username, "
//			+ "po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
//			+ "pt.processName, p_line, "
//			+ "po.recordedTime, po.duration, po.numOfWorkers, "
//			+ "lc.processStatus, lc.editStatus, po.remarks, po.personInCharge) "
//		+ "from PO po "
//			+ "join po.poCode po_code "
//				+ "join po_code.contractType t "
//				+ "join po_code.supplier s "
//			+ "join po.processType pt "
//			+ "left join po.createdBy p_user "
//			+ "left join po.productionLine p_line "
//			+ "join po.lifeCycle lc "
//		+ "where po.id = :id ")
//	Optional<PoDTO> findOrderByProcessId(Integer id);
	
	
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
