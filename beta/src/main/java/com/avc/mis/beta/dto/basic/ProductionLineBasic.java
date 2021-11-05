/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.BasicValueDTO;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.values.ProductionLine;

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
public class ProductionLineBasic extends BasicValueDTO {

	String value;
	ProductionFunctionality productionFunctionality;
	
	public ProductionLineBasic(Integer id, String value, ProductionFunctionality productionFunctionality) {
		super(id);
		this.value = value;
		this.productionFunctionality = productionFunctionality;
	}

	
	public ProductionLineBasic(ProductionLine productionLine) {
		super(productionLine.getId());
		this.value = productionLine.getValue();
		this.productionFunctionality = productionLine.getProductionFunctionality();		
	}
	
	@Override
	public ProductionLine fillEntity(Object entity) {
		ProductionLine productionLine;
		if(entity instanceof ProductionLine) {
			productionLine = (ProductionLine) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProductionLine class");
		}
		super.fillEntity(productionLine);
		productionLine.setValue(getValue());
		productionLine.setProductionFunctionality(getProductionFunctionality());
		return productionLine;
	}

}
