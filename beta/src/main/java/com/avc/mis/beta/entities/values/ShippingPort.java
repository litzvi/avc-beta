/**
 * 
 */
package com.avc.mis.beta.entities.values;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Shipping port entity - has a value for representation and a code
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name="SHIPPING_PORT")
public class ShippingPort extends ValueEntity implements ValueInterface {

	@Column(name = "name", unique = true, nullable = false)
	@NotBlank(message = "Port name(value) is mandatory")
	private String value;
	
	@Column(name = "code", unique = true, nullable = false)
	@NotBlank(message = "Shipping port has to have a code")
	private String code;
		
	public void setValue(String value) {
		this.value = Optional.ofNullable(value).map(s -> s.trim()).orElse(null);
	}
	
	/**
	 * Used by Lombok so new/transient entities with null id won't be equal.
	 * @param o
	 * @return false if both this object's and given object's id is null 
	 * or given object is not of the same class, otherwise returns true.
	 */
//	protected boolean canEqual(Object o) {
//		return Insertable.canEqualCheckNullId(this, o);
//	}
	
}
