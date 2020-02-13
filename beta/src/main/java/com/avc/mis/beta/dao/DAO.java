/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Zvi
 *
 */
@Repository
public class DAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	/**
	 * @return the jdbcTemplateObject
	 */
	protected JdbcTemplate getJdbcTemplateObject() {
		return jdbcTemplateObject;
	}
}
