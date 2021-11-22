/**
 * 
 */
package com.avc.mis.beta.utilities;

import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Building block for list of objects with an order.
 * 
 * @author zvi
 *
 */
@NoArgsConstructor
public class OrdinalAmount<T> {

	private Integer ordinal;
	private T amount;
		
	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public T getAmount() {
		return amount;
	}
	
	public void setAmount(@NonNull T amount) {
		this.amount = amount;		
	}

}
