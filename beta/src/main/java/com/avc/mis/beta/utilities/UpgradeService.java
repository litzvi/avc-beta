/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.repositories.jpa.BankRepository;

/**
 * @author Zvi
 *
 */
@Component
public class UpgradeService extends DAO {
	
	@Autowired BankRepository bankRepository;

	//should get or return a file..
	@Transactional(readOnly = true)
	public Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> backup() {
		
		Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> tablesMap = new HashMap<>();
		
		tablesMap.put(Bank.class, bankRepository.findAll());
		
		
		return tablesMap;
		
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void restore(Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> tablesMap) {
		
		List<Bank> newBanks = (List<Bank>)bankRepository.saveAll((List<Bank>)tablesMap.get(Bank.class));
		List<Bank> oldBanks = (List<Bank>) tablesMap.get(Bank.class);
		getEntityManager().flush();
//		Session session = getEntityManager().unwrap(Session.class);
		for(int i=0; i < oldBanks.size(); i++) {
//			session.replicate(oldBanks.get(i), ReplicationMode.EXCEPTION);
			Bank newBank = newBanks.get(i);
			Bank oldBank = oldBanks.get(i);
			getEntityManager().detach(newBank);
			Query query = getEntityManager().createNativeQuery("update banks set id = ? where id = ?");
			query.setParameter(1, oldBank.getId());
			query.setParameter(2, newBank.getId());
			query.executeUpdate();
//			bankRepository.setBankId(newBank.getId(), oldBank.getId());
		}
		getEntityManager().flush();
		
	}
	
	
}
