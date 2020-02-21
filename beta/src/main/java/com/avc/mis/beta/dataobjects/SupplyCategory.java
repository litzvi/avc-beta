/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name="SUPPLY_CATEGORIES")
@NamedQuery(name = "SupplyCategory.findAll", query = "select sc from SupplyCategory sc")
public class SupplyCategory {
	
	@Id @GeneratedValue
	private int id;
	
	@Column(unique = true, nullable = false)
	private String name;
	
	/*
	 * @ManyToMany(mappedBy = "supplyCategories") private Set<Supplier> suppliers;
	 */
		
}
