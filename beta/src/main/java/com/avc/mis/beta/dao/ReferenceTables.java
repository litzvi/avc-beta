/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional
public class ReferenceTables {

	@Autowired
	private JdbcTemplate jdbcTemplateObject;
	
	/**
	 * 
	 * @return
	 */
	public String getCities() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name, 'country id', countryId)) "
				+ "as cities from cities";
		return jdbcTemplateObject.queryForObject(sql, String.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCountries() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as countries\r\n" + 
				"from countries";
		return jdbcTemplateObject.queryForObject(sql, String.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCompanyPositions() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as 'company positions' \r\n" + 
				" from company_positions";
		return jdbcTemplateObject.queryForObject(sql, String.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSupplyCategories() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as 'supply categories'\r\n" + 
				"	from supply_categories";
		return jdbcTemplateObject.queryForObject(sql, String.class);
	}
	
}
