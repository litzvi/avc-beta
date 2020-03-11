/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.entities.process.OrderItem;
import com.avc.mis.beta.entities.process.PO;
import com.avc.mis.beta.entities.process.ProductionProcess;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional(rollbackFor = Throwable.class)
public class Orders extends DAO {

	public void addOrder(ProductionProcess process) {
		PO po = process.getPo();
		getEntityManager().persist(po);
		for(OrderItem item: po.getOrderItems()) {
			getEntityManager().persist(item);
		}
		getEntityManager().persist(process);
	}
}
