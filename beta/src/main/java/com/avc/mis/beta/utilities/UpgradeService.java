/**
 * 
 */
package com.avc.mis.beta.utilities;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dao.DAO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.values.Bank;
import com.avc.mis.beta.entities.values.BankBranch;
import com.avc.mis.beta.repositories.jpa.BankBranchRepository;
import com.avc.mis.beta.repositories.jpa.BankRepository;

/**
 * @author Zvi
 *
 */
@Component
public class UpgradeService extends DAO {
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	@Autowired BankRepository bankRepository;
	@Autowired BankBranchRepository branchRepository;

	//should get or return a file..
	@Transactional(readOnly = true)
	public Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> backup() {
		
		
		
		Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> tablesMap = new HashMap<>();
		
		tablesMap.put(Bank.class, bankRepository.findAll());
		tablesMap.put(BankBranch.class, branchRepository.findAll());
		
		
		
		return tablesMap;
		
	}
	
	@Transactional(rollbackFor = Throwable.class)
	public void restore(Map<Class<? extends BaseEntity>, List<? extends BaseEntity>> tablesMap) {
		
		//---save values (dependencies first e.g. bank -> branch)------------------------------------
		//------countries, cities, banks, bank branches, supply categories, company positions, items,
		//------contract types, process statuses, process types, production lines.
		//-------------------------------------------------------------------------------------------
		//---save objects (dependencies first e.g. person -> company)
		//------persons (including id card and contact details), 
		//------companies (including company contact for existing person, contact details, 
		//-----------------user entity and process type alerts)
		//-------------------------------------------------------------------------------------------
		//---save processes
		//------PO code, production process (includes: process details, e.g. PO, order items.
		//-----------------------------------excludes: user messages and approval tasks),
		//------user messages and approval tasks
		
//		List<Bank> oldBanks = (List<Bank>) tablesMap.get(Bank.class);		
		System.out.println("restoring banks");
		TreeMap<Integer, Bank> banksMap = toMap((List<Bank>)tablesMap.get(Bank.class));//sets ids to null
		List<Bank> newBanks = (List<Bank>)bankRepository.saveAll(banksMap.values());
		
		System.out.println("restoring old ids for banks");
		updateMap(banksMap, newBanks);
		updateToOldId(banksMap, "BANKS");
		
//		List<BankBranch> oldBranches = (List<BankBranch>) tablesMap.get(BankBranch.class);
		System.out.println("restoring bank branches");
		TreeMap<Integer, BankBranch> branchesMap = toMap((List<BankBranch>) tablesMap.get(BankBranch.class));//sets ids to null
		List<BankBranch> newBranches = (List<BankBranch>)branchRepository.saveAll(branchesMap.values());
		
		System.out.println("restoring old ids for branches");
		updateMap(branchesMap, newBranches);
		updateToOldId(branchesMap, "BANK_BRANCHES");
		
		
	}
	
	/**
	 * Creates a map with entity id as the key and entity with id set to null as value.
	 * Sets id to null for all objects in the list, so can save as new without checking if id already exists.
	 * @param <T>
	 * @param listToSave
	 * @return
	 */
	private <T extends Insertable> TreeMap<Integer, T> toMap(List<T> listToSave) {		
		TreeMap<Integer, T> map = new TreeMap<Integer, T>(listToSave.stream().collect(Collectors
				.toMap(t -> {return t.getId();}, t -> {t.setId(null); return t;})));
		return map;
	}
	
	private <T> void updateMap(TreeMap<Integer, T> oldMap, List<T> newList) {
		Iterator<T> itr = newList.iterator();
		for(int key: oldMap.keySet()) {
			oldMap.replace(key, itr.next());
		}
	}
	
	
	
	
	/**
	 * Sets the IDs of the newly inserted list to the old IDs of the old list. Used to restore exact previous IDs.
	 * @param <T>
	 * @param oldList
	 * @param newList
	 * @param tablename
	 */
	private <T extends ValueEntity> void updateToOldId(Map<Integer, T> map, String tablename) {
		getEntityManager().flush();
//		Connection con = getEntityManager().unwrap(Connection.class);
		
		//create list of parameters
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		map.forEach((k, v) -> {
			batchArgs.add(new Object[] {k, v.getId()});
			v.setId(k);
		});
//		for(int i=0; i < map.size(); i++) {
////			getEntityManager().detach(newEntity);
//			T newEntity = newList.get(i);
//			T oldEntity = oldList.get(i);
//			batchArgs.add(new Object[] {oldEntity.getId(), newEntity.getId()});			
//		}
		batchArgs.forEach(e -> System.out.println(e[0] + ", " + e[1]));
		String sql = "update " + tablename + " set id = ? where id = ?";
		System.out.println(jdbcTemplate.batchUpdate(sql, batchArgs)[0]);
		getEntityManager().clear();
		
//		Query query = getEntityManager().createNativeQuery(sql);
//		for(int i=0; i < oldList.size(); i++) {
////			session.replicate(oldBanks.get(i), ReplicationMode.EXCEPTION);
//			T newEntity = newList.get(i);
//			T oldEntity = oldList.get(i);
//			getEntityManager().detach(newEntity);
//			
//			query.setParameter(1, oldEntity.getId());
//			query.setParameter(2, newEntity.getId());
//			query.executeUpdate();
////			bankRepository.setBankId(newBank.getId(), oldBank.getId());
//		}
		
//		getEntityManager().flush();
	}
	
	
}
