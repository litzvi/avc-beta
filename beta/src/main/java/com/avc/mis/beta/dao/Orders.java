/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
		getEntityManager().persist(po);
	}
}
