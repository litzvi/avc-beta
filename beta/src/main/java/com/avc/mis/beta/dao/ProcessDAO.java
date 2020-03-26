/**
 * 
 */
package com.avc.mis.beta.dao;

import com.avc.mis.beta.entities.Insertable;

/**
 * @author Zvi
 *
 */
public abstract class ProcessDAO extends DAO {

	void removeEntity(Class<? extends Insertable> entityClass, Integer id) {
		Insertable entity = getEntityManager().getReference(entityClass, id);
		getEntityManager().remove(entity); 
	}
}
