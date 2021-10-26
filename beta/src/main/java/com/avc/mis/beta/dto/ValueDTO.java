/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.ValueEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO for entities who represent records that already exists in the database. e.g. country, city.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class ValueDTO extends BaseEntityDTO {

	public ValueDTO(@NonNull Integer id) {
		super(id);
	}
	
	public abstract String getValue();
	
	
	@Override
	public ValueEntity fillEntity(Object entity) {
		ValueEntity valueEntity;
		if(entity instanceof ValueEntity) {
			valueEntity = (ValueEntity) entity;
		}
		else {
			throw new IllegalArgumentException("Param has to be ValueEntity class");
		}
		super.fillEntity(valueEntity);
		
		return valueEntity;
	}
}
