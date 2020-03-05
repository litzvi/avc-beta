/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="SUPPLY_CATEGORIES")
@NamedQuery(name = "SupplyCategory.findAll", query = "select sc from SupplyCategory sc")
public class SupplyCategory implements legible, KeyIdentifiable {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String value;
	
	public void setValue(String value) {
		this.value = value.trim();
	}
	
	protected boolean canEqual(Object o) {
		return KeyIdentifiable.canEqualCheckNullId(this, o);
	}

	@Override
	public boolean isLegal() {
		return StringUtils.isNotBlank(getValue());
	}
	
	/*
	 * @ManyToMany(mappedBy = "supplyCategories") private Set<Supplier> suppliers;
	 */
		
}
