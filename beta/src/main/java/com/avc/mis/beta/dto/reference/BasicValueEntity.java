/**
 * 
 */
package com.avc.mis.beta.dto.reference;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ValueEntity;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * Generic class for holding only id and value of an entity, 
 * where all that's needed is a value to show and id for reference.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class BasicValueEntity<T extends ValueInterface> extends ValueDTO implements ValueInterface {
	
	String value;

	/**
	 * @param id
	 * @param value
	 */
	public BasicValueEntity(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	/**
	 * @param entity
	 */
	public BasicValueEntity(@NonNull T entity) {
		super(entity.getId());
		this.value = entity.getValue();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ValueEntity.class;
	}
	
	@Override
	public ValueEntity fillEntity(Object entity) {
		ValueEntity item;
		if(entity instanceof ValueEntity) {
			item = (ValueEntity) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ValueEntity class");
		}
		super.fillEntity(item);
		item.setValue(getValue());
		
		return item;
	}

}
