/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Company position entity - has a name-value for different types of positions.
 * e.g. manager, accountant etc.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="COMPANY_POSITIONS")
public class CompanyPosition extends ValueEntity {
	
	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Company position name(value) is mandatory")
	private String value;

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
