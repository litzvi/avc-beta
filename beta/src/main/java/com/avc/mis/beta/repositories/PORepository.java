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
import com.avc.mis.beta.dto.values.PoBasic;
import com.avc.mis.beta.dto.values.PoRow;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessName;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.values.ProcessType;

/**
 * @author Zvi
 *
 */
public interface PORepository extends BaseRepository<PO> {
	
	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user, "
			+ "po_code, po.processType, p_line, "
			+ "po.recordedTime, po.duration, po.numOfWorkers, "
			+ "p_status, po.remarks, "
			+ "s.id, s.version, s.name, "
			+ "po.orderStatus) "
		+ "from PO po "
			+ "left join po.poCode po_code "
			+ "left join po.supplier s "
			+ "left join po.user p_user "
			+ "left join po.productionLine p_line "
			+ "left join po.status p_status "
		+ "where po_code.id = :codeId ")
	Optional<PoDTO> findOrderByPoCodeId(Integer codeId);
	
	@Query("select new com.avc.mis.beta.dto.process.PoDTO("
			+ "po.id, po.version, po.createdDate, p_user, "
			+ "po_code, po.processType, p_line, "
			+ "po.recordedTime, po.duration, po.numOfWorkers, "
			+ "p_status, po.remarks, "
			+ "s.id, s.version, s.name, "
			+ "po.orderStatus) "
		+ "from PO po "
			+ "left join po.poCode po_code "
			+ "left join po.supplier s "
			+ "left join po.user p_user "
			+ "left join po.productionLine p_line "
			+ "left join po.status p_status "
		+ "where po.id = :id ")
	Optional<PoDTO> findOrderByProcessId(Integer id);
	
	@Query("select new com.avc.mis.beta.dto.process.OrderItemDTO("
			+ "i.id, i.version, po.id, item, i.measureUnit, i.numberUnits, i.currency, "
			+ "i.unitPrice, i.deliveryDate, i.defects, i.remarks) "
		+ "from OrderItem i "
		+ "left join i.item item "
		+ "join i.po po "
		+ "where po.id = :poid ")
	Set<OrderItemDTO> findOrderItemsByPo(Integer poid);
	
	@Query("select new com.avc.mis.beta.dto.values.PoBasic(po.id, po_code, s.name, po.orderStatus) "
		+ "from PO po "
		+ "left join po.poCode po_code "
		+ "left join po.supplier s "
		+ "left join po.processType t "
		+ "where t.value = ?1 and po.orderStatus in ?2 ")
	List<PoBasic> findByOrderTypeAndStatusesBasic(ProcessName orderType, OrderStatus[] statuses);
	
	@Query("select new com.avc.mis.beta.dto.values.PoRow(po.id, po_code, s.name, i.value, "
			+ "oi.numberUnits, oi.measureUnit, po.recordedTime, oi.deliveryDate, po.orderStatus) "
		+ "from PO po "
		+ "left join po.poCode po_code "
		+ "left join po.supplier s "
		+ "left join po.processType t "
		+ "join po.orderItems oi "
			+ "left join oi.item i "
		+ "where t.value = ?1 and po.orderStatus in ?2 ")
	List<PoRow> findByOrderTypeAndStatuses(ProcessName orderType, OrderStatus[] statuses);

	@Query("select t from ProcessType t where t.value = :value")
	ProcessType findProcessTypeByValue(ProcessName value);
	
}
