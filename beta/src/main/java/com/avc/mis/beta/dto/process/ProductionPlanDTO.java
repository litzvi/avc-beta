/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.group.ProductionPlanRowDTO;
import com.avc.mis.beta.dto.process.info.ProductionPlanInfo;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.ProductionPlan;
import com.avc.mis.beta.entities.process.group.ProductionPlanRow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class ProductionPlanDTO extends GeneralProcessDTO {
	
	private List<ProductionPlanRowDTO> productionPlanRows;
	
	public void setProductionPlanInfo(ProductionPlanInfo info) {		
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProductionPlan.class;
	}
	
	@Override
	public ProductionPlan fillEntity(Object entity) {
		ProductionPlan productionPlan;
		if(entity instanceof ProductionPlan) {
			productionPlan = (ProductionPlan) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProductionPlan class");
		}
		super.fillEntity(productionPlan);
		
		if(getProductionPlanRows() != null) {
			productionPlan.setProductionPlanRows(getProductionPlanRows().stream()
					.map(i -> i.fillEntity(new ProductionPlanRow())).collect(Collectors.toSet()));
		}
		
		return productionPlan;
	}

}
