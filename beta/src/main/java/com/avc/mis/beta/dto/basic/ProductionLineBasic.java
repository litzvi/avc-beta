/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProductionLineBasic extends ValueDTO {

	String value;
	ProductionFunctionality productionFunctionality;
	
	public ProductionLineBasic(Integer id, String value, ProductionFunctionality productionFunctionality) {
		super(id);
		this.value = value;
		this.productionFunctionality = productionFunctionality;
	}

}
