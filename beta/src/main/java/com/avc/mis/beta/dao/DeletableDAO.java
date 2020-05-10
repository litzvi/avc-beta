/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.ProcessAlert;

/**
 * Data access object for objects that could be permanently deleted from underlying persistence context.
 * Should be used cautiously, if added for testing should be removed at production.
 * 
 * @author Zvi
 *
 */
@Repository
public class DeletableDAO extends DAO {

	/**
	 * Used for permanently removed from persistence context - deleted from the database.
	 * @param entity to be removed 
	 * @throws IllegalArgumentException , TransactionRequiredException, EntityNotFoundException
	 */
	public void permenentlyRemoveEntity(BaseEntity entity) {
		permenentlyRemoveEntity(entity.getClass(), entity.getId());
	}
	
	/**
	 * Used for permanently removed from persistence context - deleted from the database.
	 * @param entityClass the class of the entity
	 * @param id of the removed entity
	 * @throws IllegalArgumentException , TransactionRequiredException, EntityNotFoundException
	 */
	public void permenentlyRemoveEntity(Class<? extends BaseEntity> entityClass, Integer id) {
		Insertable entity = getEntityManager().getReference(entityClass, id);
		getEntityManager().remove(entity); 
	}
	
}
