/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
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
				
		String sql = "insert into suppliers (companyId) values (?)";
		jdbcTemplateObject.update(sql, new Object[] {supplier.getId()});
		
		SupplyCategory[] categories = supplier.getSupplyCategories();
		if(categories != null) {
			List<Object[]> batchArgs = new ArrayList<Object[]>();
			for(SupplyCategory category: categories) {
				if(category.getId() != null) {
					batchArgs.add(new Object[] {supplier.getId(), category.getId()});
				}
			}			
			sql = "insert into category_suppliers (companyId, categoryId) values (?, ?)";
			jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER, Types.INTEGER});
			
		}
		
	}

	/**
	 * @param jdbcTemplateObject
	 */
	public void editSupplier(JdbcTemplate jdbcTemplateObject) {
		if(getId() == null) {
			throw new IllegalArgumentException("Supplier id can't be null");
		}
		if(getSupplyCategories() != null) {
			// TODO delete all records for supplier and add the new list.
		}
		super.editCompany(jdbcTemplateObject);
	}

	
	
}
