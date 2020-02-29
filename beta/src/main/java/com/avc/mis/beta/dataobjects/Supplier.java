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
@NamedQuery(name = "Supplier.findAll", query = "select s from Supplier s")
@NamedQuery(name = "Supplier.details", 
	query = "select s from Supplier s "
			+ "left join fetch s.contactDetails cd "
			+ "where s.id = :sid ")
@NamedQuery(name = "CompanyContact.details.findAll", 
	query = "select cc from CompanyContact cc "
			+ "left join fetch cc.person p "
				+ "left join fetch p.idCard id "
				+ "left join fetch p.contactDetails cd "
			+ "left join fetch cc.position "
			+ "where cc.company.id = :cid ")
@NamedQuery(name = "Supplier.details.old", 
	query = "select s from Supplier s "
			+ "left join fetch s.contactDetails cd "
				+ "left join cd.phones pn "
				+ "left join cd.faxes f "
				+ "left join cd.emails e "
				+ "left join cd.addresses a "
					+ "left join a.city "
				+ "left join cd.paymentAccounts pa "
					+ "left join pa.bankAccount ba "
						+ "left join ba.branch bb "
							+ "left join bb.bank b "
			+ "left join s.companyContacts cc "
				+ "left join cc.person p "
					+ "left join p.idCard id "
						+ "left join id.nationality "
					+ "left join p.contactDetails pcd "
						+ "left join pcd.phones ppn "
						+ "left join pcd.faxes pf "
						+ "left join pcd.emails pe "
						+ "left join pcd.addresses pa "
							+ "left join pa.city "
				+ "left join cc.position pos "
			+ "left join s.supplyCategories sc "
			+ "where s.id = :sid ")

//@NamedNativeQuery(name = "Supplier.findAllBasic", 
//	query = "select s.id as id, s.name as name from Company s", resultSetMapping = "BasicSupplier")
//@SqlResultSetMapping(name = "BasicSupplier", columns = {@ColumnResult(name = "id"), @ColumnResult(name = "name")})
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
