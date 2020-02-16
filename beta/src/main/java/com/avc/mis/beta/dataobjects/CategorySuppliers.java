/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
/*
 * Maybe Class should be removed
 * @Entity 
 * @Table(name="CATEGORY_SUPPLIERS")
 */public class CategorySuppliers {
	
	private int companyId;
	private int categoryId;
	
	
	public void addCategorySuppliers(JdbcTemplate jdbcTemplateObject) {
		String sql = "insert into category_suppliers (companyId, categoryId) values (?, ?)";
		jdbcTemplateObject.update(sql, new Object[] {companyId, categoryId});
	}
	
	public void removeCategorySuppliers(JdbcTemplate jdbcTemplateObject) {
		//TODO
	}
}
