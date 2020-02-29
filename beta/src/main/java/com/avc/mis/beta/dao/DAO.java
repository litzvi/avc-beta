/**
 * 
 */
package com.avc.mis.beta.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
