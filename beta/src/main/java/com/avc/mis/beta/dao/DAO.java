/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.avc.mis.beta.entities.Insertable;

/**
 * @author Zvi
 *
 */
@Repository
public abstract class DAO {
	
	public static final int BATCH_SIZE = 20;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * @return the jdbcTemplateObject
	 */
	protected JdbcTemplate getJdbcTemplateObject() {
		return jdbcTemplateObject;
	}

	/**
	 * @return the entityManager
	 */
	EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected void addEntity(Insertable entity, Insertable reference) {
		reference = getEntityManager().getReference(reference.getClass(), reference.getId());
		entity.setReference(reference);
		getEntityManager().persist(entity);
	}
	
	protected void removeEntity(Insertable entity) {
		entity = getEntityManager().getReference(entity.getClass(), entity.getId());
		getEntityManager().remove(entity); 
	}
	
	protected void editEntity(Insertable entity) {
		if(entity.getId() == null) {
			throw new IllegalArgumentException("Received wrong id, entity can't be found in database");
		}
		getEntityManager().merge(entity);
	}	
}
