/**
 * 
 */
package com.avc.mis.beta.dto.plan;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.enums.Shift;
import com.avc.mis.beta.entities.plan.ProcessItemPlan;
import com.avc.mis.beta.entities.plan.ProductionPlanRow;
import com.avc.mis.beta.entities.plan.UsedItemPlan;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ProductionPlanRowDTO extends DataDTO {

	private BasicValueEntity<ProcessType> processType;	
	private BasicValueEntity<ProductionLine> productionLine;	
	private Shift shift;
	private Integer numOfWorkers;
	private LocalDate plannedDate;
	
	private List<ProcessItemPlanDTO> processItemPlans;	
	private List<UsedItemPlanDTO> usedItemPlans;
	
	
	public ProductionPlanRowDTO(Integer id, Integer version, 
			Integer processTypeId, String processTypeValue,
			Integer productionLineId, String productionLineValue, 
			Shift shift, Integer numOfWorkers, LocalDate plannedDate) {
		super(id, version);
		this.processType = new BasicValueEntity<ProcessType>(processTypeId, processTypeValue);
		this.productionLine = new BasicValueEntity<ProductionLine>(productionLineId, productionLineValue);
		this.shift = shift;
		this.numOfWorkers = numOfWorkers;
		this.plannedDate = plannedDate;
	}
	
	public ProductionPlanRowDTO(ProductionPlanRow productionPlanRow) {
		super(productionPlanRow);
		ProcessType processType = productionPlanRow.getProcessType();
		if(processType != null) {
			this.processType = new BasicValueEntity<ProcessType>(processType.getId(), processType.getValue());
		}
		ProductionLine productionLine = productionPlanRow.getProductionLine();
		if(productionLine != null) {
			this.productionLine = new BasicValueEntity<ProductionLine>(productionLine.getId(), productionLine.getValue());
		}
		this.shift = productionPlanRow.getShift();
		this.numOfWorkers = productionPlanRow.getNumOfWorkers();
		this.plannedDate = productionPlanRow.getPlannedDate();
		
		if(productionPlanRow.getProcessItemPlans() != null) {
			this.processItemPlans = productionPlanRow.getProcessItemPlans().stream()
					.map(i -> new ProcessItemPlanDTO(i))
					.sorted(Ordinal.ordinalComparator())
					.collect(Collectors.toList());		
		}
		
		if(productionPlanRow.getUsedItemPlans() != null) {
			this.usedItemPlans = productionPlanRow.getUsedItemPlans().stream()
					.map(i -> new UsedItemPlanDTO(i))
					.sorted(Ordinal.ordinalComparator())
					.collect(Collectors.toList());		
		}
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProductionPlanRow.class;
	}
	
	@Override
	public ProductionPlanRow fillEntity(Object entity) {
		ProductionPlanRow productionPlanRow;
		if(entity instanceof ProductionPlanRow) {
			productionPlanRow = (ProductionPlanRow) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProductionPlanRow class");
		}
		super.fillEntity(productionPlanRow);
		if(getProcessType() != null) {
			ProcessType processType = new ProcessType();
			processType.setId(getProcessType().getId());
			productionPlanRow.setProcessType(processType);		
		}
		if(getProcessType() != null) {
			ProductionLine productionLine = new ProductionLine();
			productionLine.setId(getProductionLine().getId());
			productionPlanRow.setProductionLine(productionLine);
		}
		productionPlanRow.setShift(shift);
		productionPlanRow.setNumOfWorkers(numOfWorkers);
		productionPlanRow.setPlannedDate(plannedDate);
		
		if(getProcessItemPlans() != null) {
			Ordinal.setOrdinals(getProcessItemPlans());
			productionPlanRow.setProcessItemPlans(getProcessItemPlans().stream().map(i -> i.fillEntity(new ProcessItemPlan())).collect(Collectors.toSet()));
		}
		
		if(getUsedItemPlans() != null) {
			Ordinal.setOrdinals(getUsedItemPlans());
			productionPlanRow.setUsedItemPlans(getUsedItemPlans().stream().map(i -> i.fillEntity(new UsedItemPlan())).collect(Collectors.toSet()));
		}

		return productionPlanRow;
	}


	public void setPlannedDate(String plannedDate) {
		if(plannedDate != null)
			this.plannedDate = LocalDate.parse(plannedDate);
	}

}
