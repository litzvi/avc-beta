/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Generic class for holding a BasicValueEntity object with an id of of it's owner object.
 * Same idea as ValueObject but can be used in select constructor (in database query) if only BasicValueEntity needed.
 * Used where we need to reference lists of BasicValueEntity objects, where each references the id of it's wrapping object.
 * Convenient for directly fetching only BasicValueEntity with foreign key from database.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = false, callSuper = true)
public class ValueEntityObject<T extends ValueInterface> extends ValueDTO {
	
	BasicValueEntity<T> value;

	public ValueEntityObject(Integer id, BasicValueEntity<T> value) {
		super(id);
		this.value = value;
	}
	
	public ValueEntityObject(Integer id, Integer valueId, String value) {
		super(id);
		this.value = new BasicValueEntity<T>(valueId, value);
	}
}
