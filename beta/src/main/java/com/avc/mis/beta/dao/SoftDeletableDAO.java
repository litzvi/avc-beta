/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.entities.SoftDeleted;

/**
 * Acts as a buffer for entities that shouldn't be physically deleted.
 * 
 * @author Zvi
 *
 */
@Transactional(rollbackFor = Throwable.class)
public abstract class SoftDeletableDAO extends DAO {

	/**
	 * Soft remove (delete) of entity - only flags the entity as not active without physically deleting.
	 * @param entity the entity to be removed.
	 * @throws IllegalArgumentException
	 */
	void removeEntity(SoftDeleted entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		entity.setActive(false);
		getEntityManager().merge(entity); 
	}

}
