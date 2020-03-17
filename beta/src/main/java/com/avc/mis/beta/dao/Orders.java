/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.PoDTO;
import com.avc.mis.beta.dto.PoRow;
import com.avc.mis.beta.entities.enums.OrderStatus;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Orders extends DAO {
	
	
	private List<PO> findPurchaseOrders() {
		TypedQuery<PO> query = getEntityManager().createNamedQuery("PO.findAll", PO.class);
		List<PO> orders = query.getResultList();
		return orders;
	}
	
	private List<Object[]> findPurchaseOrders(ProcessType orderType, OrderStatus[] statuses) {
		TypedQuery<Object[]> query = getEntityManager()
				.createNamedQuery("PO&ITEM.findByOrderTypeAndStatus", Object[].class);
		query.setParameter("type", orderType);
		query.setParameter("statuses", statuses);
		List<Object[]> orders = query.getResultList();
		return orders;
	}
	
	public List<PoRow> findCashewOrders(OrderStatus[] statuses) {
		List<Object[]> orders = findPurchaseOrders(ProcessType.CASHEW_ORDER, statuses);
		return orders.stream().map(result -> {return new PoRow((PO)result[0], (OrderItem)result[1]);})
			.collect(Collectors.toList());
	}
	
	public List<PoRow> findGeneralOrders(OrderStatus[] statuses) {
		List<Object[]> orders = findPurchaseOrders(ProcessType.GENERAL_ORDER, statuses);
		return orders.stream().map(result -> {return new PoRow((PO)result[0], (OrderItem)result[1]);})
			.collect(Collectors.toList());
	}

	public void addCashewOrder(PO po) {
		po.getOrderProcess().setProcessType(ProcessType.CASHEW_ORDER);
		
		//using save rather than persist in case POid was assigned by user
		Session session = getEntityManager().unwrap(Session.class);
		session.save(po);
//		if(po.getId() != null)
//			getEntityManager().merge(po);
//		else
//			getEntityManager().persist(po);
	}
	
	public void addGeneralOrder(PO po) {
		po.getOrderProcess().setProcessType(ProcessType.GENERAL_ORDER);
		
		//using save rather than persist in case POid was assigned by user
		Session session = getEntityManager().unwrap(Session.class);
		session.save(po);
	}
	
	public PoDTO getOrder(int orderId) {
		TypedQuery<PO> queryPurchaseOrder = getEntityManager().createNamedQuery("PO.details", PO.class);
		queryPurchaseOrder.setParameter("poid", orderId);
		PO order;
		try {
			order = queryPurchaseOrder.getSingleResult();
		}
		catch(NoResultException e) {
			throw new IllegalArgumentException("No order with given PO code");
		}
		return new PoDTO(order);
	}
	
	public void editOrder(PO po) {
		editEntity(po);
	}
	
	public void removeOrder(int orderId) {
		PO order = getEntityManager().getReference(PO.class, orderId);
		getEntityManager().remove(order);
	}
	
	
	
	
}
