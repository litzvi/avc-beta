/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.enums.SupplyGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Supply group entity - used for distinguishing the items that can be supplied by different kinds of suppliers.
 * Used to filter out items for purchase orders of a supplier.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="SUPPLY_CATEGORIES")
@NamedQuery(name = "SupplyCategory.findAll", query = "select sc from SupplyCategory sc")
public class SupplyCategory extends ValueEntity {
	
	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Supply category name(value) can't be blank")
	private String value;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NotNull(message = "Supply group is mandatory")
	private SupplyGroup supplyGroup;
	
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
	protected boolean canEqual(Object o) {
		return Insertable.canEqualCheckNullId(this, o);
	}
	
}
