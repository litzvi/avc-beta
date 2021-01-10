/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
public class WeightedObject<T> {

	private BigDecimal weight;
	private T object;
	
	public WeightedObject(BigDecimal weight, T object) {
		super();
		this.weight = weight;
		this.object = object;
	}
	
	
}
