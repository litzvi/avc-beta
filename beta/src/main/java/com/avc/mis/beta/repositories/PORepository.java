/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.generic.ValueObject;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.process.collection.OrderItemDTO;
import com.avc.mis.beta.dto.process.collection.ProcessFileDTO;
import com.avc.mis.beta.dto.processInfo.OrderProcessInfo;
import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.view.PoItemRow;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.enums.ProcessStatus;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.collection.OrderItem;

/**
 * Spring repository for accessing purchase order information.
 * 
 * @author Zvi
 *
 */
public interface PORepository extends PoProcessRepository<PO> {

	
	@Query("select new com.avc.mis.beta.dto.processInfo.OrderProcessInfo(po.closed) "
		+ "from PO po "
		+ "where po.id = :processId ")
	OrderProcessInfo findPoInfo(Integer processId);
	
	/**
	 * Gets all OrderItems for a given process in a OrderItemDTO that contains order 
	 * information and amount received.
	 * @param processId the process id of the PO
	 * @return Set of OrderItemDTOs for the given process
	 */
	@Query("select new com.avc.mis.beta.dto.process.collection.OrderItemDTO("
			+ "i.id, i.version, i.ordinal, "
			+ "item.id, item.value, item.measureUnit, "
			+ "units.amount, units.measureUnit, "
			+ "price.amount, price.currency, i.deliveryDate, i.defects, i.remarks, "
			+ "SUM( "
				+ "CASE "
					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
					+ "ELSE (coalesce(sf.unitAmount, 1) * sf.numberUnits * uom.multiplicand / uom.divisor) "
				+ "END) ) "
		+ "from OrderItem i "
			+ "join i.po po "
			+ "join i.numberUnits units "
			+ "left join i.receiptItems ri "
				+ "left join ri.process r "
					+ "left join r.lifeCycle rlc "		
				+ "left join ri.storageForms sf "
					+ "left join sf.group sf_group "
						+ "left join UOM uom "
							+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = units.measureUnit "
			+ "left join i.unitPrice price "
			+ "join i.item item "
		+ "where po.id = :processId "
		+ "group by i "
		+ "order by i.ordinal ")
	List<OrderItemDTO> findPoOrderItemsById(Integer processId);

	/**
	 * Gets the PO details in a PoDTO object by process id (exclusive) or po code id
	 * @param processId the process id of the PO
	 * @param poCodeId po code id of the PO
	 * @return PoDTO a DTO of a PO with all process information.
	 */
	@Query("select distinct new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user.username, "
			+ "po_code.id, po_code.code, t.code, t.suffix, s.id, s.version, s.name, "
			+ "pt.processName, p_line.id, p_line.value, p_line.productionFunctionality,  "
			+ "po.recordedTime, po.shift, po.startTime, po.endTime, po.downtime, po.numOfWorkers, po.personInCharge, "
			+ "lc.processStatus, lc.editStatus, po.remarks, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(u.username, ':', approval.decision))), "
			+ "po.closed) "
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
		+ "where ((po.id = :processId and :poCodeId is null) "
				+ "or (:processId is null and  po_code.id = :poCodeId)) "
			+ "and lc.processStatus in :statuses "
//		+ "where po.id = :processId or po_code.id = :poCodeId "
//			+ "and (:processId is null or :poCodeId is null) "
//			+ "and lc.processStatus in :statuses "
		+ "group by po "
		+ "order by lc.processStatus ")
	Optional<PoDTO> findOrderById(Integer processId, Integer poCodeId, ProcessStatus[] statuses);
	
	@Query("select new com.avc.mis.beta.dto.report.ItemAmount("
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item.productionUse, "
			+ "item_unit.amount, item_unit.measureUnit, type(item), "
//			+ "SUM(units.amount * uom.multiplicand / uom.divisor)) "
			+ "(units.amount "
				+ " - "
				+ "coalesce(SUM("
					+ "CASE "
						+ "WHEN (:pointOfTime is null or r.recordedTime <= :pointOfTime) "
							+ "and rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
						+ "THEN (rou.amount * rou_uom.multiplicand / rou_uom.divisor) "
						+ "ELSE null " 
					+ "END), "
				+ "0)) * uom.multiplicand / uom.divisor) "
		+ "from PO po "
			+ "join po.lifeCycle lc "
			+ "join po.processType t "
			+ "join po.orderItems oi "
				+ "join oi.numberUnits units "
				+ "join oi.item item "
					+ "join item.unit item_unit "
				+ "join UOM uom "
					+ "on uom.fromUnit = units.measureUnit and uom.toUnit = item.measureUnit "
				+ "left join oi.receiptItems ri "
					+ "left join ri.process r "
						+ "left join r.lifeCycle rlc "		
					+ "left join ri.receivedOrderUnits rou "
						+ "left join UOM rou_uom "
							+ "on rou_uom.fromUnit = rou.measureUnit and rou_uom.toUnit = units.measureUnit "
		+ "where "
			+ "(t.processName = :orderType or :orderType is null) "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and (lc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED) "
//			+ "and (rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL) "
			+ "and (:pointOfTime is null "
				+ "or po.recordedTime <= :pointOfTime) "
		+ "group by oi, units "
		+ "having coalesce("
			+ "SUM("
				+ "CASE "
					+ "WHEN (:pointOfTime is null or r.recordedTime <= :pointOfTime) "
						+ "and rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.FINAL "
					+ "THEN (rou.amount * rou_uom.multiplicand / rou_uom.divisor) "
					+ "ELSE null " 
				+ "END), "
//				+ "(rou.amount * rou_uom.multiplicand / rou_uom.divisor)), "
			+ "0) < units.amount ")
	List<ItemAmount> findOpenOrPendingReceiptOrdersItemAmounts(ProcessName orderType, ItemGroup itemGroup, LocalDateTime pointOfTime);

	/**
	 * Gets rows of all orders (history) for the given order type with their order status. 
	 * @param orderType e.g. GENERAL_ORDER, CASHEW_ORDER
	 * @return List of PoItemRow for all orders.
	 */
	@Query("select new com.avc.mis.beta.dto.view.PoItemRow(po.id, po.closed, po.personInCharge, "
			+ "po_code.id, po_code.code, ct.code, ct.suffix, s.name, "
			+ "function('GROUP_CONCAT', function('DISTINCT', concat(coalesce(user.username, ''), ': ', coalesce(approval.decision, '')))), "
			+ "item.id, item.value, item.measureUnit, item.itemGroup, item_unit, type(item), "
			+ "oi.id, units.amount, units.measureUnit, po.recordedTime, oi.deliveryDate, "
			+ "oi.defects, price.amount, price.currency, "
//			+ "SUM( "
//				+ "CASE "
//					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
//					+ "ELSE (rou.amount * rou_uom.multiplicand / rou_uom.divisor) "
//				+ "END "
//			+ "), "
//			+ "SUM( "
//				+ "CASE "
//					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN null  "
//					+ "ELSE (sf.unitAmount * sf.numberUnits * uom.multiplicand / uom.divisor) "
//				+ "END "
//			+ "), "
			+ "lc.processStatus"
//			+ ", "
//			+ "SUM( "
//				+ "CASE "
//					+ "WHEN rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED THEN 1 "
//					+ "ELSE 0 "
//				+ "END "
//			+ ") "
		+ ") "
		+ "from PO po "
			+ "join po.lifeCycle lc "
			+ "join po.poCode po_code "
				+ "join po_code.contractType ct "
				+ "join po_code.supplier s "
			+ "join po.processType t "
			+ "join po.orderItems oi " 
				+ "join oi.numberUnits units "
				+ "left join oi.unitPrice price "
				+ "join oi.item item "
					+ "join item.unit item_unit "
//				+ "left join oi.receiptItems ri "
//					+ "left join ri.process r "
//						+ "left join r.lifeCycle rlc "		
			+ "left join po.approvals approval "
				+ "left join approval.user user "
//					+ "left join ri.storageForms sf "
////						+ "left join sf.group sf_group "
//							+ "left join UOM uom "
//								+ "on uom.fromUnit = ri.measureUnit and uom.toUnit = units.measureUnit "
//					+ "left join ri.receivedOrderUnits rou "
//						+ "left join UOM rou_uom "
//							+ "on rou_uom.fromUnit = rou.measureUnit and rou_uom.toUnit = units.measureUnit "
		+ "where "
			+ "(t.processName = :orderType or :orderType is null) "
			+ "and (item.itemGroup = :itemGroup or :itemGroup is null) "
			+ "and lc.processStatus in :processStatuses "
			+ "and (po_code.id = :poCodeId or :poCodeId is null) "
			+ "and (:startTime is null or oi.deliveryDate >= :startTime) "
			+ "and (:endTime is null or oi.deliveryDate < :endTime) "
			+ "and ( "
					+ ":onlyOpen = false "
				+ "or (po.closed = false "
					+ "and "
					+ "coalesce("
					+ "(select sum(rou_2.amount * rou_uom_2.multiplicand / rou_uom_2.divisor) "
					+ "from oi.receiptItems ri_2 "
							+ "join ri_2.process r_2 "
								+ "join r_2.lifeCycle rlc_2 "		
							+ "join ri_2.receivedOrderUnits rou_2 "
								+ "join UOM rou_uom_2 "
									+ "on rou_uom_2.fromUnit = rou_2.measureUnit and rou_uom_2.toUnit = units.measureUnit "
					+ "where rlc_2.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED), 0) "
					+ " < "
					+ "units.amount) "
			+ ")"
		+ "group by oi "
		+ "ORDER BY oi.deliveryDate, po.id ")
	List<PoItemRow> findAllOrdersByType(
			ProcessName orderType, ProcessStatus[] processStatuses, Integer poCodeId, ItemGroup itemGroup, boolean onlyOpen, 
			LocalDate startTime, LocalDate endTime);


	@Query("select oi "
			+ "from OrderItem oi "
				+ "join oi.numberUnits nu "
				+ "join oi.receiptItems ri "
					+ "join ri.process r "
						+ "join r.lifeCycle rlc "		
					+ "join ri.receivedOrderUnits rnu "
						+ "join UOM uom "
							+ "on uom.fromUnit = rnu.measureUnit and uom.toUnit = nu.measureUnit "
//					+ "join ri.storageForms sf "
//						+ "join sf.group sf_group "
//							+ "join UOM uom "
//								+ "on uom.fromUnit = sf_group.measureUnit and uom.toUnit = nu.measureUnit "
			+ "where oi.id in :orderItemIds "
				+ "and rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
			+ "group by oi "
//			+ "having nu.amount <= sum(sf.unitAmount * sf.numberUnits * uom.multiplicand / uom.divisor) "
			+ "having nu.amount <= sum(rnu.amount * uom.multiplicand / uom.divisor) ")
	List<OrderItem> findNonOpenOrderItemsById(Integer[] orderItemIds);

	@Query("select new com.avc.mis.beta.dto.generic.ValueObject( "
			+ "oi.id, "
			+ "SUM(coalesce(sf.unitAmount, 1) * sf.numberUnits * uom.multiplicand / uom.divisor)) "
		+ "from OrderItem oi "
			+ "join oi.numberUnits units "
			+ "join oi.receiptItems ri "
				+ "join ri.process r "
					+ "join r.lifeCycle rlc "		
				+ "join ri.storageForms sf "
//					+ "left join sf.group sf_group "
						+ "join UOM uom "
							+ "on uom.fromUnit = ri.measureUnit and uom.toUnit = units.measureUnit "
		+ "where "
			+ "oi.id in :orderItemIds "
			+ "and rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
		+ "group by oi ")
	Stream<ValueObject<BigDecimal>> findReceivedAmountByOrderItemIds(int[] orderItemIds);

	@Query("select new com.avc.mis.beta.dto.generic.ValueObject( "
			+ "oi.id, "
			+ "SUM(rou.amount * rou_uom.multiplicand / rou_uom.divisor)) "
		+ "from OrderItem oi "
			+ "join oi.numberUnits units "
			+ "join oi.receiptItems ri "
				+ "join ri.process r "
					+ "join r.lifeCycle rlc "
				+ "join ri.receivedOrderUnits rou "
				+ "join UOM rou_uom "
					+ "on rou_uom.fromUnit = rou.measureUnit and rou_uom.toUnit = units.measureUnit "
		+ "where "
			+ "oi.id in :orderItemIds "
			+ "and rlc.processStatus <> com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
		+ "group by oi ")
	Stream<ValueObject<BigDecimal>> findReceivedOrderUnitsByOrderItemIds(int[] orderItemIds);

	@Query("select new com.avc.mis.beta.dto.generic.ValueObject( "
			+ "oi.id, "
			+ "SUM(1)) "
		+ "from OrderItem oi "
			+ "join oi.receiptItems ri "
				+ "join ri.process r "
					+ "join r.lifeCycle rlc "		
		+ "where "
			+ "oi.id in :orderItemIds "
			+ "and rlc.processStatus = com.avc.mis.beta.entities.enums.ProcessStatus.CANCELLED "
		+ "group by oi ")
	Stream<ValueObject<Long>> findumReceiptsCancelledByOrderItemIds(int[] orderItemIds);


	
}
