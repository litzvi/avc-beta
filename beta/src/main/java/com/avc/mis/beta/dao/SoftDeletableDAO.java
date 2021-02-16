/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.SoftDeleted;

/**
 * Acts as a buffer for entities that shouldn't be physically deleted.
 * 
 * @author Zvi
 *
 */
@Repository
public class SoftDeletableDAO extends DAO {

	
	/**
	 * SOFT DELETE ENTITY BY CLASS AND ID
	 * Soft remove (delete) of entity - only flags the entity as not active without physically deleting.
	 * @param entityClass the class of entity to be removed.
	 * @param entityId id of entity to be removed.
	 * @throws IllegalArgumentException
	 */
	public <T extends BaseEntity & SoftDeleted> void removeEntity(Class<T> entityClass, int entityId) {
		T entity = getEntityManager().getReference(entityClass, entityId);
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		
	    CriteriaUpdate<T> update = 
	    		getEntityManager().getCriteriaBuilder().createCriteriaUpdate(entityClass);
	    Root<T> root = update.from(entityClass);
	    getEntityManager().createQuery(update.
	    		set("active", false).where(root.get("id").in(entityId))).executeUpdate();
	   	
	}


	
}
