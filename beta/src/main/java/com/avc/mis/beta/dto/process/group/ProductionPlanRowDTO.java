/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.process.collectionItems.ProcessItemPlanDTO;
import com.avc.mis.beta.dto.process.collectionItems.UsedItemPlanDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.enums.Shift;
import com.avc.mis.beta.entities.process.collectionItems.ProcessItemPlan;
import com.avc.mis.beta.entities.process.collectionItems.UsedItemPlan;
import com.avc.mis.beta.entities.process.group.ProductionPlanRow;
import com.avc.mis.beta.entities.values.ProcessType;
import com.avc.mis.beta.entities.values.ProductionLine;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO owned by production plan. 
 * Contains a plan for processing/manufacturing given items at a given production line at a given time.
 * 
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

	public void setPlannedDate(String plannedDate) {
		if(plannedDate != null)
			this.plannedDate = LocalDate.parse(plannedDate);
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


}
