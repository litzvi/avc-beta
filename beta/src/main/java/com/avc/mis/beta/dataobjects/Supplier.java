/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.springframework.jdbc.core.JdbcTemplate;

import com.avc.mis.beta.dao.DAO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "SUPPLIERS")
@PrimaryKeyJoinColumn(name = "companyId")
@NamedQuery(name = "Supplier.findAll",query = "select s from Supplier s "
		+ "left join fetch s.contactDetails cd ")
@NamedQuery(name = "Supplier.details", 
	query = "select s from Supplier s "
			+ "left join fetch s.contactDetails cd "
			+ "where s.id = :sid ")
@NamedQuery(name = "CompanyContact.details.findAll", 
	query = "select cc from CompanyContact cc "
			+ "left join fetch cc.position "
			+ "left join fetch cc.person p "
				+ "left join fetch p.idCard id "
				+ "left join fetch p.contactDetails cd "
			+ "where cc.company.id = :cid ")
public class Supplier extends Company {
	
	@JoinTable(name = "SUPPLIERS_CATEGORIES",
			joinColumns = @JoinColumn(name = "companyId", referencedColumnName = "companyId"), 
			inverseJoinColumns = @JoinColumn(name = "categoryId", referencedColumnName = "id"))
	@ManyToMany(fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<SupplyCategory> supplyCategories = new HashSet<>();


	/*
		*//**
			 * 
			 * @param supplier
			 * @return
			 */
	
	public static void insertSupplier(JdbcTemplate jdbcTemplateObject, Supplier
	supplier) {
	
		/*
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
		 */
	
	}
	
	/**
		 * @param jdbcTemplateObject
		 */
		public void editSupplier(JdbcTemplate jdbcTemplateObject) { 
		/*
		 * if(getId() == null) { throw new
		 * IllegalArgumentException("Supplier id can't be null"); }
		 * if(getSupplyCategories() != null) { // TODO delete all records for supplier
		 * and add the new list. } super.editCompany(jdbcTemplateObject);
		 */
		}

	
	
			
		
		
}
