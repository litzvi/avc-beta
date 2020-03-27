/**
 * 
 */
package com.avc.mis.beta.dao;

import com.avc.mis.beta.entities.SoftDeleted;

/**
 * @author Zvi
 *
 */
public abstract class SoftDeletableDAO extends DAO {

	//public -- only for testing
	public void softDeleteEntity(SoftDeleted entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		entity.setActive(false);
		getEntityManager().merge(entity); 
	}

}
