/**
 * 
 */
package com.avc.mis.beta.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avc.mis.beta.dataobjects.City;
import com.avc.mis.beta.dataobjects.CompanyPosition;
import com.avc.mis.beta.dataobjects.Country;
import com.avc.mis.beta.dataobjects.SupplyCategory;

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
	
	/**
	 * 
	 * @return
	 */
	public List<SupplyCategory> getAllSupplyCategories() {
		
		TypedQuery<SupplyCategory> query = getEntityManager().createNamedQuery(
				"SupplyCategory.findAll", SupplyCategory.class);
		return query.getResultList();
	}
	
	public List<City> getAllCities() {
		
		TypedQuery<City> query = getEntityManager().createNamedQuery(
				"City.findAll", City.class);
		return query.getResultList();
	}
	
	public List<Country> getAllCountries() {
		
		TypedQuery<Country> query = getEntityManager().createNamedQuery(
				"Country.findAll", Country.class);
		return query.getResultList();
	}

	public List<CompanyPosition> getAllCompanyPositions() {
		
		TypedQuery<CompanyPosition> query = getEntityManager().createNamedQuery(
				"CompanyPosition.findAll", CompanyPosition.class);
		return query.getResultList();
	}
	
	/**
	 * @param category 
	 * 
	 */
	public void insertSupplyCategory(SupplyCategory category) {
		getEntityManager().persist(category);
//		getEntityManager().flush();
		
	}
	
}
