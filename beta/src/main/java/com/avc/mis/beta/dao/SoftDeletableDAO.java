/**
 * 
 */
package com.avc.mis.beta.dao;

import com.avc.mis.beta.entities.SoftDeleted;

/**
 * Acts as a buffer for entities that shouldn't be physically deleted.
 * 
 * @author Zvi
 *
 */
public abstract class SoftDeletableDAO extends DAO {

	void removeEntity(SoftDeleted entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		entity.setActive(false);
		getEntityManager().merge(entity); 
	}

}
