/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
public class OrdinalAmount<T> {

	private Integer ordinal;
	private T amount;
	
	public OrdinalAmount(Integer ordinal, @NonNull T amount) {
		this.ordinal = ordinal;
		this.amount = amount;
	}
	
	public void setAmount(@NonNull T amount) {
		this.amount = amount;		
	}
}
