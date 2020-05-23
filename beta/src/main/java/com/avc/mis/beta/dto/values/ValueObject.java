/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.ValueEntity;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ValueObject extends ValueDTO {
	
	String value;

	public ValueObject(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	public ValueObject(@NonNull ValueEntity entity) {
		super(entity.getId());
		this.value = entity.getValue();
	}
}
