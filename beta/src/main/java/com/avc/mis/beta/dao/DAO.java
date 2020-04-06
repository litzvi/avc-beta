/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.repositories.ProcessRepository;
import com.avc.mis.beta.repositories.ReferenceTablesRepository;

/**
 * @author Zvi
 *
 */
public abstract class DAO {
	
	public static final int BATCH_SIZE = 20;
	
	@Autowired private ReferenceTablesRepository referenceRepository;
	@Autowired private ProcessRepository processRepository;
	@Autowired private EntityManager entityManager;
	

	/**
	 * @return the processRepository
	 */
	ProcessRepository getProcessRepository() {
		return processRepository;
	}

	/**
	 * @return the baseRepository
	 */
	ReferenceTablesRepository getReferenceRepository() {
		return referenceRepository;
	}

	/**
	 * @return the entityManager
	 */
	EntityManager getEntityManager() {
		return entityManager;
	}
	
	//public -- only for testing
	public void addEntity(Insertable entity, Insertable reference) {
		reference = getEntityManager().getReference(reference.getClass(), reference.getId());
		entity.setReference(reference);
		getEntityManager().persist(entity);
	}
	
	void removeEntity(Insertable entity) {
		entity = getEntityManager().getReference(entity.getClass(), entity.getId());
		getEntityManager().remove(entity); 
	}
	
	void removeEntity(Class<? extends Insertable> entityClass, Integer id) {
		Insertable entity = getEntityManager().getReference(entityClass, id);
		getEntityManager().remove(entity); 
	}
	
	//public -- only for testing
	public Insertable editEntity(Insertable entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		return getEntityManager().merge(entity);
	}	
}
