/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.avc.mis.beta.dao.DAO;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Supplier extends Company implements KeyIdentifiable{
	
	@JoinTable(name = "SUPPLIERS_CATEGORIES",
			joinColumns = @JoinColumn(name = "companyId", referencedColumnName = "companyId"), 
			inverseJoinColumns = @JoinColumn(name = "categoryId", referencedColumnName = "id"))
	@ManyToMany(fetch = FetchType.LAZY)
	@BatchSize(size = DAO.BATCH_SIZE)
	private Set<SupplyCategory> supplyCategories = new HashSet<>();

	@JsonIgnore
	@Override
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, this.getClass().getSuperclass(), o);
	}	
		
}
