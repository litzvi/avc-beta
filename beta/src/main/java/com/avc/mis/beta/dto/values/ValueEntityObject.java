/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.ValueInterface;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
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
