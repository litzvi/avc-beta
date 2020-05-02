/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.repositories.ProcessInfoRepository;
import com.avc.mis.beta.repositories.ValueTablesRepository;
import com.avc.mis.beta.utilities.UserAware;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for data access objects services(dao).
 * DAO object are spring components, used as services to access different module entities data.
 * This class holds entity manager and repository components needed to access data in multiple modules.
 * Also implements basic add, edit and remove (persist, merge and remove) of entities.

 * @author Zvi
 *
 */
@Getter(value = AccessLevel.PROTECTED)
@Transactional(rollbackFor = Throwable.class)
public abstract class DAO {
	
	@Autowired private ValueTablesRepository valueTablesRepository;
	@Autowired private ProcessInfoRepository processRepository;
	@Autowired private EntityManager entityManager;
	@Autowired UserAware userAware;
	
	/**
	 * Gets the logged in user id.
	 * @return the id of currently logged in user.
	 * @throws IllegalStateException if logged in UserEntity not available.
	 */
	Integer getCurrentUserId() {
		Optional<UserLogin> userEntity = userAware.getCurrentUser();
		userEntity.orElseThrow(() -> new IllegalStateException("No user logged in or user not reachable"));
		return userEntity.get().getId();
	}
	
	/**
	 * Used for adding entity that references a detached entity.
	 * @param entity the entity to persisted.
	 * @param reference detached entity referenced by entity (the owner of the association).
	 * @throws IllegalArgumentException, EntityNotFoundException, EntityExistsException, TransactionRequiredException
	 */
	//public only for testing
	public void addEntity(BaseEntity entity, BaseEntity reference) {
		reference = getEntityManager().getReference(reference.getClass(), reference.getId());
		entity.setReference(reference);
		addEntity(entity);
	}
	
	/**
	 * Adding an entity that all it's associations are currently managed.
	 * @param entity to be persisted
	 * @throws IllegalArgumentException, EntityExistsException, TransactionRequiredException
	 */
	void addEntity(BaseEntity entity) {
		getEntityManager().persist(entity);
	}
	
	/**
	 * Used for permanently removed from persistence context - deleted from the database.
	 * @param entity to be removed 
	 * @throws IllegalArgumentException , TransactionRequiredException, EntityNotFoundException
	 */
	//public only for testing
	public void permenentlyRemoveEntity(BaseEntity entity) {
		permenentlyRemoveEntity(entity.getClass(), entity.getId());
	}
	
	/**
	 * Used for permanently removed from persistence context - deleted from the database.
	 * @param entityClass the class of the entity
	 * @param id of the removed entity
	 * @throws IllegalArgumentException , TransactionRequiredException, EntityNotFoundException
	 */
	void permenentlyRemoveEntity(Class<? extends BaseEntity> entityClass, Integer id) {
		Insertable entity = getEntityManager().getReference(entityClass, id);
		getEntityManager().remove(entity); 
	}
	
	/**
	 * @param entity the entity with edited state information including id.
	 * @return the newly edited entity
	 * @throws IllegalArgumentException if instance is not an entity,  
	 * dosen't have an id set or is a removed entity.
	 * TransactionRequiredException
	 */
	//public for testing
	public BaseEntity editEntity(BaseEntity entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		return getEntityManager().merge(entity);
	}	
}
