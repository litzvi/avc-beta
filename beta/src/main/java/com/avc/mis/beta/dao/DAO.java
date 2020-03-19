/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.entities.BaseEntityNoId;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.repositories.BaseRepository;
import com.avc.mis.beta.repositories.PORepository;
import com.avc.mis.beta.repositories.ReferenceTablesRepository;
import com.avc.mis.beta.repositories.SupplierRepository;

/**
 * @author Zvi
 *
 */
@Repository
public abstract class DAO {
	
	public static final int BATCH_SIZE = 20;
	
	
//	@Autowired private PORepository poRepository;
//	@Autowired private SupplierRepository supplierRepository;	
	@Autowired private ReferenceTablesRepository referenceRepository;
	@Autowired private EntityManager entityManager;
	

	/**
	 * @return the baseRepository
	 */
	protected ReferenceTablesRepository getReferenceRepository() {
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
	
	void editEntity(Insertable entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		getEntityManager().merge(entity);
	}	
}
