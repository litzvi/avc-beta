/**
 * 
 */
package com.avc.mis.beta.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zvi
 *
 */
@Repository
@Transactional
public class ReferenceTables extends DAO {

	
	/**
	 * 
	 * @return
	 */
	public String getCities() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name, 'country id', countryId)) "
				+ "as cities from cities";
		return getJdbcTemplateObject().queryForObject(sql, String.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCountries() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as countries\r\n" + 
				"from countries";
		return getJdbcTemplateObject().queryForObject(sql, String.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCompanyPositions() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as 'company positions' \r\n" + 
				" from company_positions";
		return getJdbcTemplateObject().queryForObject(sql, String.class);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSupplyCategories() {
		String sql = "select JSON_ARRAYAGG(JSON_OBJECT('id', id, 'name', name)) as 'supply categories'\r\n" + 
				"	from supply_categories";
		return getJdbcTemplateObject().queryForObject(sql, String.class);
	}
	
}
