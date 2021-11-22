/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.ProductionFunctionality;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Shows partial information of a production line.
 * Used for reference and display.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProductionLineDTO extends ValueDTO {

	private String value;
	private ProductionFunctionality productionFunctionality;
	
	public ProductionLineDTO(Integer id, String value, ProductionFunctionality productionFunctionality) {
		super(id);
		this.value = value;
		this.productionFunctionality = productionFunctionality;
	}
	
	public ProductionLineDTO(ProductionLine productionLine) {
		super(productionLine.getId());
		this.value = productionLine.getValue();
		this.productionFunctionality = productionLine.getProductionFunctionality();		
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProductionLine.class;
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
