/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.entities.Ordinal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
public class OrdinalObject<T> {

	private Integer ordinal;
	private T value;
	
	public OrdinalObject(Integer ordinal, @NonNull T value) {
		this.ordinal = ordinal;
		this.value = value;
	}
	
	public void setValue(@NonNull T value) {
		this.value = value;		
	}
}
