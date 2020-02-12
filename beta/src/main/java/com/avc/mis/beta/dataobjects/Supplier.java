/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class Supplier extends Company {
	
	private SupplyCategory[] supplyCategories;
	
	/**
	 * @param id
	 * @param name
	 * @param localName
	 * @param englishName
	 * @param license
	 * @param taxCode
	 * @param registrationLocation
	 * @param contactDetails
	 * @param companyContacts
	 */
	public Supplier(Integer id, String name, String localName, String englishName, String license, String taxCode,
			String registrationLocation, ContactDetails contactDetails, CompanyContact[] companyContacts, 
			SupplyCategory[] supplyCategories) {
		super(id, name, localName, englishName, license, taxCode, registrationLocation, contactDetails, companyContacts);
		this.supplyCategories = supplyCategories;
	}
	
	/**
	 * @param name
	 * @param localName
	 * @param englishName
	 * @param license
	 * @param taxCode
	 * @param registrationLocation
	 * @param contactDetails
	 * @param companyContacts
	 */
	public Supplier(String name, String localName, String englishName, String license, String taxCode,
			String registrationLocation, ContactDetails contactDetails, CompanyContact[] companyContacts, 
			SupplyCategory[] supplyCategories) {
		super(name, localName, englishName, license, taxCode, registrationLocation, contactDetails, companyContacts);
		this.supplyCategories = supplyCategories;
	}
	
	/**
	 * 
	 * @param supplier
	 * @return
	 */
	public static void insertSupplier(JdbcTemplate jdbcTemplateObject, Supplier supplier) {
		
		Company.insertCompany(jdbcTemplateObject, supplier);
				
		String sql = "insert into suppliers (companyID) values (?)";
		jdbcTemplateObject.update(sql, new Object[] {supplier.getId()});
		
		SupplyCategory[] categories = supplier.getSupplyCategories();
		if(categories != null) {
			sql = "insert into category_suppliers (companyId, categoryId) values (?, ?)";
			jdbcTemplateObject.batchUpdate(sql, 
					new BatchPreparedStatementSetter() {
			            
						public void setValues(PreparedStatement ps, int i) throws SQLException {
			                ps.setInt(1, supplier.getId());
			                ps.setInt(2, categories[i].getId());
			            }
			
			            public int getBatchSize() {
			                return categories.length;
			            }
	        		});
		}
		
	}

	/**
	 * @param jdbcTemplateObject
	 */
	public void editSupplier(JdbcTemplate jdbcTemplateObject) {
		
	}

	
	
}
