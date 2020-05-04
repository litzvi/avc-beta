/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.entities.SoftDeleted;
import com.avc.mis.beta.entities.data.Supplier;

/**
 * Acts as a buffer for entities that shouldn't be physically deleted.
 * 
 * @author Zvi
 *
 */
@Repository
//@Transactional(rollbackFor = Throwable.class)
public class SoftDeletableDAO extends DAO {

	/**
	 * Soft remove (delete) of entity - only flags the entity as not active without physically deleting.
	 * @param entity the entity to be removed.
	 * @throws IllegalArgumentException
	 */
	public void removeEntity(SoftDeleted entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		entity.setActive(false);
		getEntityManager().merge(entity); 
	}
	
	public void removeEntity(Class<? extends SoftDeleted> entityClass, int entityId) {
		SoftDeleted entity = getEntityManager().getReference(entityClass, entityId);
		removeEntity(entity);	
	}

}
