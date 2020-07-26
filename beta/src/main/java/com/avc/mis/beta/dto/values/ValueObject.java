/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = false, callSuper = true)
public class ValueObject<T> extends ValueDTO {

	T value;

	public ValueObject(Integer id, T value) {
		super(id);
		this.value = value;
	}
}
