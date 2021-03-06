/**
 * 
 */
package com.avc.mis.beta.entities.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.entities.values.SupplyCategory;

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
public class Supplier extends Company {
	
	@JoinTable(name = "SUPPLIERS_CATEGORIES",
			joinColumns = @JoinColumn(name = "companyId", referencedColumnName = "companyId"), 
			inverseJoinColumns = @JoinColumn(name = "categoryId", referencedColumnName = "id"))
	@ManyToMany(fetch = FetchType.LAZY)
//	@BatchSize(size = BaseEntity.BATCH_SIZE)
	private Set<SupplyCategory> supplyCategories = new HashSet<>();
		
}
