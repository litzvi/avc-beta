/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "SUPPLIERS")
@PrimaryKeyJoinColumn(name = "companyId")
public class Supplier extends Company {
	
	
	@Column(columnDefinition = "boolean default true", nullable = false)
	private boolean isActive;

	@JoinTable(name = "SUPPLIERS_CATEGORIES", 
			joinColumns = @JoinColumn(name = "companyId", referencedColumnName = "companyId"), 
			inverseJoinColumns = @JoinColumn(name = "categoryId", referencedColumnName = "id"))
	@ManyToMany
	private Set<SupplyCategory> supplyCategories;

	/*
		*//**
			 * 
			 * @param supplier
			 * @return
			 */
	/*
	 * public static void insertSupplier(JdbcTemplate jdbcTemplateObject, Supplier
	 * supplier) {
	 * 
	 * Company.insertCompany(jdbcTemplateObject, supplier);
	 * 
	 * String sql = "insert into suppliers (companyId) values (?)";
	 * jdbcTemplateObject.update(sql, new Object[] {supplier.getId()});
	 * 
	 * SupplyCategory[] categories = supplier.getSupplyCategories2(); if(categories
	 * != null) { List<Object[]> batchArgs = new ArrayList<Object[]>();
	 * for(SupplyCategory category: categories) { if(category.getId() != null) {
	 * batchArgs.add(new Object[] {supplier.getId(), category.getId()}); } } sql =
	 * "insert into category_suppliers (companyId, categoryId) values (?, ?)";
	 * jdbcTemplateObject.batchUpdate(sql, batchArgs, new int[]{Types.INTEGER,
	 * Types.INTEGER});
	 * 
	 * }
	 * 
	 * }
	 * 
	 *//**
		 * @param jdbcTemplateObject
		 *//*
			 * public void editSupplier(JdbcTemplate jdbcTemplateObject) { if(getId() ==
			 * null) { throw new IllegalArgumentException("Supplier id can't be null"); }
			 * if(getSupplyCategories() != null) { // TODO delete all records for supplier
			 * and add the new list. } super.editCompany(jdbcTemplateObject); }
			 * 
			 * 
			 */
}
