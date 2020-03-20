/**
 * 
 */
package com.avc.mis.beta.services;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.repositories.ReferenceTablesRepository;

/**
 * @author Zvi
 *
 */
@Repository
public abstract class DAO {
	
	public static final int BATCH_SIZE = 20;
	
	@Autowired private ReferenceTablesRepository referenceRepository;
	@Autowired private EntityManager entityManager;
	

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
	
	Insertable editEntity(Insertable entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		return getEntityManager().merge(entity);
	}	
}
