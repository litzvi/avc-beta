/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.PoDTO;
import com.avc.mis.beta.dto.SupplierDTO;
import com.avc.mis.beta.entities.data.Supplier;
import com.avc.mis.beta.entities.enums.ProcessType;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Orders extends DAO {

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
	
	public PoDTO getOrder(int id) {
		TypedQuery<PO> queryPurchaseOrder = getEntityManager().createNamedQuery("PO.details", PO.class);
		queryPurchaseOrder.setParameter("poid", id);
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
	
	
}
