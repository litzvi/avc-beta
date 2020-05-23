/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.avc.mis.beta.dto.process.OrderItemDTO;
import com.avc.mis.beta.dto.process.PoDTO;
import com.avc.mis.beta.dto.values.PoRow;
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
			+ "po_code.code, t.code, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "po.recordedTime, po.duration, po.numOfWorkers, "
			+ "p_status, po.remarks, "
			+ "po.orderStatus) "
		+ "from PO po "
			+ "join po.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join po.processType pt "
			+ "left join po.createdBy p_user "
			+ "left join po.productionLine p_line "
			+ "left join po.status p_status "
		+ "where po_code.id = :codeId ")
	Optional<PoDTO> findOrderByPoCodeId(Integer codeId);
	
	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user.username, "
			+ "po_code.code, t.code, s.id, s.version, s.name, "
			+ "pt.processName, p_line, "
			+ "po.recordedTime, po.duration, po.numOfWorkers, "
			+ "p_status, po.remarks, "
			+ "po.orderStatus) "
		+ "from PO po "
			+ "join po.poCode po_code "
				+ "join po_code.contractType t "
				+ "join po_code.supplier s "
			+ "join po.processType pt "
			+ "left join po.createdBy p_user "
			+ "left join po.productionLine p_line "
			+ "left join po.status p_status "
		+ "where po.id = :id ")
	Optional<PoDTO> findOrderByProcessId(Integer id);
	
	@Query("select new com.avc.mis.beta.dto.process.OrderItemDTO("
			+ "i.id, i.version, item.id, item.value, i.measureUnit, i.numberUnits, i.currency, "
			+ "i.unitPrice, i.deliveryDate, i.defects, i.remarks, ri is not null) "
		+ "from OrderItem i "
			+ "left join ReceiptItem ri on ri.orderItem = i "
		+ "join i.item item "
		+ "join i.po po "
		+ "where po.id = :poid ")
	Set<OrderItemDTO> findOrderItemsByPo(Integer poid);
	
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

	@Query("select new com.avc.mis.beta.dto.values.PoRow(po.id, po_code.code, ct.code, s.name, i.value, "
			+ "oi.numberUnits, oi.measureUnit, po.recordedTime, oi.deliveryDate, po.orderStatus, "
			+ "oi.defects, oi.currency, oi.unitPrice) "
		+ "from PO po "
		+ "join po.poCode po_code "
			+ "join po_code.contractType ct "
			+ "join po_code.supplier s "
		+ "join po.processType t "
		+ "join po.orderItems oi "
			+ "join oi.item i "
		+ "where not exists (select ri from ReceiptItem ri where ri.orderItem = oi) "
			+ "and t.processName = ?1 "
		+ "ORDER BY oi.deliveryDate DESC ")
	List<PoRow> findOpenOrderByType(ProcessName orderType);

}
