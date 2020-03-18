/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.PoBasic;
import com.avc.mis.beta.dto.PoDTO;
import com.avc.mis.beta.dto.PoRow;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.repositories.PORepository;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(readOnly = true)
public class Orders extends DAO {
			
	public List<PoRow> findCashewOrders(OrderStatus[] statuses) {
		return getPoRepository().findByOrderTypeAndStatuses(ProcessType.CASHEW_ORDER, statuses);
	}
	
	public List<PoRow> findGeneralOrders(OrderStatus[] statuses) {
		return getPoRepository().findByOrderTypeAndStatuses(ProcessType.GENERAL_ORDER, statuses);
	}
	
	public List<PoBasic> findCashewOrdersBasic(OrderStatus[] statuses) {
		return getPoRepository().findByOrderTypeAndStatusesBasic(ProcessType.CASHEW_ORDER, statuses);		
	}
	
	public List<PoBasic> findGeneralOrdersBasic(OrderStatus[] statuses) {
		return getPoRepository().findByOrderTypeAndStatusesBasic(ProcessType.GENERAL_ORDER, statuses);
	}
	
	private void addOrder(PO po) {
		//using save rather than persist in case POid was assigned by user
		Session session = getEntityManager().unwrap(Session.class);
		session.save(po);
	}

	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addCashewOrder(PO po) {
		po.getOrderProcess().setProcessType(ProcessType.CASHEW_ORDER);
		addOrder(po);		
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void addGeneralOrder(PO po) {
		po.getOrderProcess().setProcessType(ProcessType.GENERAL_ORDER);
		addOrder(po);	
	}
	
	public PoDTO getOrder(int orderId) {
//		TypedQuery<PO> queryPurchaseOrder = getEntityManager().createNamedQuery("PO.details", PO.class);
//		queryPurchaseOrder.setParameter("poid", orderId);
//		PO order;
//		try {
//			order = queryPurchaseOrder.getSingleResult();
//		}
//		catch(NoResultException e) {
//			throw new IllegalArgumentException("No order with given PO code");
//		}
		Optional<PoDTO> order = getPoRepository().findOrderById(orderId);
		PoDTO po = order.orElseThrow(
				()->new IllegalArgumentException("No order with given PO code"));
		po.setOrderItems(getPoRepository().findOrderItemsByPo(orderId));
		
		return po;

//		return new PoDTO(order.orElseThrow(
//				()->new IllegalArgumentException("No order with given PO code")));
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void editOrder(PO po) {
		editEntity(po);
	}
	
	@Transactional(rollbackFor = Throwable.class, readOnly = false)
	public void removeOrder(int orderId) {
		getPoRepository().deleteById(orderId);
//		PO order = getEntityManager().getReference(PO.class, orderId);
//		getEntityManager().remove(order);
	}
	
	
	
	
}
