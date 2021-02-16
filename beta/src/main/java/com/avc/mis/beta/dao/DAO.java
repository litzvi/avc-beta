/**
 * 
 */
package com.avc.mis.beta.dao;

import java.security.AccessControlException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.avc.mis.beta.dto.data.UserLogin;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.UserEntity;
import com.avc.mis.beta.service.Orders;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Abstract base class for modifying data access objects repositories(dao).
 * DAO object are spring components, used as repositories to access different module entities data.
 * This class has an entity manager needed to access data in multiple modules.
 * Also implements basic add, edit and remove (persist, merge and remove) of entities.

 * @author Zvi
 *
 */
@Getter(value = AccessLevel.PACKAGE)
public abstract class DAO extends ReadDAO {

	@Autowired private EntityManager entityManager;
	@Autowired private PasswordEncoder encoder;

		
	/**
	 * Used for adding entity that references a detached entity.
	 * @param entity the entity to persisted.
	 * @param reference detached entity referenced by entity (the owner of the association).
	 * @throws IllegalArgumentException, EntityNotFoundException, EntityExistsException, TransactionRequiredException
	 */
	public void addEntity(BaseEntity entity, BaseEntity reference) {
		setEntityReference(entity, reference.getClass(), reference.getId());
		addEntity(entity);
	}
	
	/**
	 * Used for adding entity that references a detached entity, with referenced entity id.
	 * @param <T> extends BaseEntity
	 * @param entity the entity to persisted.
	 * @param referenceClass class of detached entity referenced by entity (the owner of the association).
	 * @param referenceId id of detached entity referenced by entity (the owner of the association).
	 * @throws IllegalArgumentException, EntityNotFoundException, EntityExistsException, TransactionRequiredException
	 */
	public <T extends BaseEntity> void addEntity(BaseEntity entity, Class<T> referenceClass, Integer referenceId) {
		setEntityReference(entity, referenceClass, referenceId);
		addEntity(entity);
	}
	
	/**
	 * Adding an entity that all it's associations are currently managed.
	 * @param entity to be persisted
	 * @throws IllegalArgumentException, EntityExistsException, TransactionRequiredException
	 */
	public void addEntity(BaseEntity entity) {
		getEntityManager().persist(entity);
	}
		
	/**
	 * Sets the reference for the entity so it's ready for manipulation by the persistence manager.
	 * @param <T>
	 * @param entity the entity holds a reference.
	 * @param referenceClass class of detached entity referenced by entity (the owner of the association).
	 * @param referenceId id of detached entity referenced by entity (the owner of the association).
	 */
	public <T extends BaseEntity> void setEntityReference(
			BaseEntity entity, Class<T> referenceClass, Integer referenceId) {
		T reference = getEntityManager().getReference(referenceClass, referenceId);
		entity.setReference(reference);
	}

	/**
	 * EDIT (MERGE) ENTITY
	 * @param entity the entity with edited state information including id.
	 * @return the newly edited entity
	 * @throws IllegalArgumentException if instance is not an entity,  
	 * dosen't have an id set or is a removed entity.
	 */
	public <T extends BaseEntity> T editEntity(T entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		/*
		 * using update saves running the selects before updating, but updates even if nothing
		 * was changed therefore affecting the modifying date and user modifying.
		 */
//		Session session = getEntityManager().unwrap(Session.class); 
//		session.update(entity);
		return getEntityManager().merge(entity);
	}
	

	/**
	 * Gets a list of entities and make sure they are all merged to persistence.
	 * Whether new or edited, will be persisted or edited accordingly.
	 * @param entityList
	 */
	public <T extends BaseEntity> void addOrEdit(List<T> entityList) {
		entityList.forEach(entity -> getEntityManager().merge(entity));
	}
	
	/**
	 * Gets a list of entities to persist to database.
	 * @param entityList
	 */
	public <T extends BaseEntity> void addAll(List<T> entityList) {
		entityList.forEach(entity -> getEntityManager().persist(entity));
	}
	
	/**
	 * Search for an entity of the specified class and primary key. 
	 * @param entityClass entity class
	 * @param id primary key
	 * @return the found entity instance or null if the entity does not exist
	 * @throws IllegalArgumentException if id is null
	 */
	public <T extends BaseEntity> T find(Class<T> entityClass, Integer id) {
		return getEntityManager().find(entityClass, id);
	}
	
	/**
	 * Changes password for current user.
	 * Provided with the correct current password and the new pasword.
	 * @param password the current password
	 * @param newPassword the new password
	 * @return number of rows changed
	 */
	public int changeUserPassword(String password, String newPassword) {
		
		UserLogin user = getCurrentUser();
		String encodedPassword = user.getPassword();
		if(encoder.matches(password, encodedPassword)) {
			CriteriaUpdate<UserEntity> update = 
		    		getEntityManager().getCriteriaBuilder().createCriteriaUpdate(UserEntity.class);
		    Root<UserEntity> root = update.from(UserEntity.class);
		    return getEntityManager().createQuery(update.
		    		set("password", encoder.encode(newPassword)).where(root.get("id").in(user.getId()))).executeUpdate();
		}		
		else {
			throw new AccessControlException("Couldn't change password: wrong password");
		}
	}	

	/**
	 * Adds an entity with Hibernate session.save().
	 * Used in {@link Orders.addOrder} in order to let Hibernate to decide if to auto generate key.
	 * @param entity to be persisted
	 */
	@Deprecated
	public void addEntityWithFlexibleGenerator(BaseEntity entity) {
		Session session = getEntityManager().unwrap(Session.class);
		session.save(entity);
	}


}
