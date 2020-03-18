/**
 * 
 */
package com.avc.mis.beta.repositories;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.dto.OrderItemDTO;
import com.avc.mis.beta.dto.PoBasic;
import com.avc.mis.beta.dto.PoDTO;
import com.avc.mis.beta.dto.PoRow;
import com.avc.mis.beta.entities.data.Item;
import com.avc.mis.beta.entities.data.Staff;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.ContractType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProcessStatus;
import com.avc.mis.beta.entities.process.ProductionLine;

/**
 * @author Zvi
 *
 */
public interface PORepository extends JpaRepository<PO, Integer> {
	
	@Query("select new com.avc.mis.beta.dto.PoDTO("
			+ "po.id, c_type, s.id, s.name, po.status, p.id, p.insertTime, "
			+ "p_staff, p.processType, p_line, p.time, p.duration, "
			+ "p.numOfWorkers, p_status, p.remarks) "
		+ "from PO po "
			+ "left join po.contractType c_type "
			+ "left join po.supplier s "
			+ "left join po.orderProcess p "
				+ "left join p.staffRecording p_staff "
				+ "left join p.productionLine p_line "
				+ "left join p.status p_status "
		+ "where po.id = :id ")
	Optional<PoDTO> findOrderById(Integer id);
	
	@Query("select new com.avc.mis.beta.dto.OrderItemDTO("
			+ "i.id, po.id, item, i.numberUnits, i.currency, "
			+ "i.unitPrice, i.deliveryDate, i.defects, i.remarks) "
		+ "from OrderItem i "
		+ "left join i.item item "
		+ "join i.po po "
		+ "where po.id = :poid ")
	List<OrderItemDTO> findOrderItemsByPo(Integer poid);
	
	@Query("select new com.avc.mis.beta.dto.PoBasic(po.id, t.value, s.name, po.status) "
		+ "from PO po "
		+ "left join po.contractType t "
		+ "left join po.supplier s "
		+ "left join po.orderProcess p "
		+ "where p.processType = :orderType and po.status in :statuses ")
	List<PoBasic> findByOrderTypeAndStatusesBasic(ProcessType orderType, OrderStatus[] statuses);
	
	@Query("select new com.avc.mis.beta.dto.PoRow(po.id, t.value, s.name, i.value, "
			+ "oi.numberUnits, i.measureUnit, p.time, oi.deliveryDate, po.status) "
		+ "from PO po "
		+ "left join po.contractType t "
		+ "left join po.supplier s "
		+ "left join po.orderProcess p "
		+ "left join po.orderItems oi "
			+ "left join oi.item i "
		+ "where p.processType = :orderType and po.status in :statuses ")
	List<PoRow> findByOrderTypeAndStatuses(ProcessType orderType, OrderStatus[] statuses);
	
}
