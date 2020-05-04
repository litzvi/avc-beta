/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dto.values.UserLogin;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.process.PO;
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
@Getter(value = AccessLevel.PACKAGE)
//@Transactional(rollbackFor = Throwable.class)
public abstract class DAO extends ReadDAO {
	
//	@Autowired private ValueTablesRepository valueTablesRepository;
//	@Autowired private ProcessInfoRepository processRepository;
	@Autowired private EntityManager entityManager;
		
	/**
	 * Used for adding entity that references a detached entity.
	 * @param entity the entity to persisted.
	 * @param reference detached entity referenced by entity (the owner of the association).
	 * @throws IllegalArgumentException, EntityNotFoundException, EntityExistsException, TransactionRequiredException
	 */
	//public only for testing
	public void addEntity(BaseEntity entity, BaseEntity reference) {
		setEntityReference(entity, reference.getClass(), reference.getId());
//		reference = getEntityManager().getReference(reference.getClass(), reference.getId());
//		entity.setReference(reference);
		addEntity(entity);
	}
	
	/**
	 * Used for adding entity that references a detached entity, with referenced entity id.
	 * @param <T> extends BaseEntity
	 * @param entity the entity to persisted.
	 * @param referenceClass class of detached entity referenced by entity (the owner of the association).
	 * @param referenceId id of detached entity referenced by entity (the owner of the association).
	 */
	//public only for testing
	public <T extends BaseEntity> void addEntity(BaseEntity entity, Class<T> referenceClass, Integer referenceId) {
		setEntityReference(entity, referenceClass, referenceId);
//		T reference = getEntityManager().getReference(referenceClass, referenceId);
//		entity.setReference(reference);
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
	 * Adds an entity with Hibernate session.save().
	 * Used in {@link Orders.addOrder} in order to let Hibernate to decide if to auto generate key.
	 * @param entity to be persisted
	 */
	public void addEntityWithFlexibleGenerator(BaseEntity entity) {
		Session session = getEntityManager().unwrap(Session.class);
		session.save(entity);
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
	 * @param entity the entity with edited state information including id.
	 * @return the newly edited entity
	 * @throws IllegalArgumentException if instance is not an entity,  
	 * dosen't have an id set or is a removed entity.
	 * TransactionRequiredException
	 */
	public BaseEntity editEntity(BaseEntity entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		return getEntityManager().merge(entity);
	}	
}
