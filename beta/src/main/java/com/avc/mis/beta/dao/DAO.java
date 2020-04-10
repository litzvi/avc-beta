/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.repositories.ReferenceTablesRepository;

/**
 * Base class for data access objects services(dao).
 * DAO object are spring components, used as services to access different module entities data.
 * This class holds entity manager and repository components needed to access data in multiple modules.
 * Also implements basic add, edit and remove (persist, merge and remove) of entities.

 * @author Zvi
 *
 */
public abstract class DAO {
	
	public static final int BATCH_SIZE = 20;
	
	@Autowired private ReferenceTablesRepository referenceRepository;
	@Autowired private ProcessInfoRepository processRepository;
	@Autowired private EntityManager entityManager;
	

	/**
	 * @return the processRepository
	 */
	ProcessInfoRepository getProcessRepository() {
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
	
	void addEntity(Insertable entity, Insertable reference) {
		reference = getEntityManager().getReference(reference.getClass(), reference.getId());
		entity.setReference(reference);
		addEntity(entity);
	}
	
	void addEntity(Insertable entity) {
		getEntityManager().persist(entity);
	}
	
	@Deprecated
	void permenentlyRemoveEntity(Insertable entity) {
		entity = getEntityManager().getReference(entity.getClass(), entity.getId());
		getEntityManager().remove(entity); 
	}
	
	@Deprecated
	void permenentlyRemoveEntity(Class<? extends Insertable> entityClass, Integer id) {
		Insertable entity = getEntityManager().getReference(entityClass, id);
		getEntityManager().remove(entity); 
	}
	
	Insertable editEntity(Insertable entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		return getEntityManager().merge(entity);
	}	
}
