/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.entities.Ordinal;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class OrdinalObject<T> {

	Integer ordinal;
	T value;
	
	public OrdinalObject(Integer ordinal, T value) {
		this.ordinal = ordinal;
		this.value = value;
	}
}
