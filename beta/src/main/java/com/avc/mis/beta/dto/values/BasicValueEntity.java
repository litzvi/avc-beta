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
public class BasicValueEntity<T extends ValueEntity> extends ValueDTO {
	
	String value;

	public BasicValueEntity(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	public BasicValueEntity(@NonNull T entity) {
		super(entity.getId());
		this.value = entity.getValue();
	}
}
