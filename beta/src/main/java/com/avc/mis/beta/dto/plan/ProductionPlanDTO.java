/**
 * 
 */
package com.avc.mis.beta.dto.plan;

import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.GeneralProcessDTO;
import com.avc.mis.beta.dto.processInfo.ProductionPlanInfo;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.plan.ProductionPlan;
import com.avc.mis.beta.entities.plan.ProductionPlanRow;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

	public ProductionPlanDTO(@NonNull ProductionPlan productionPlan) {
		super(productionPlan);
		if(productionPlan.getProductionPlanRows() != null) {
			this.productionPlanRows = productionPlan.getProductionPlanRows().stream()
					.map(i -> new ProductionPlanRowDTO(i))
					.collect(Collectors.toList());		
		}
	}
	
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

	
	@Override
	public String getProcessTypeDescription() {
		return "Production Plan";	
	}


}
