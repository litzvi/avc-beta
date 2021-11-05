/**
 * 
 */
package com.avc.mis.beta.dto.plan;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.plan.ProcessItemPlan;

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
public class ProcessItemPlanDTO extends RankedAuditedDTO {
	@EqualsAndHashCode.Exclude
	private Integer planId;
	private BasicValueEntity<Item> item;
	private AmountWithUnit numberUnits;
	
	public ProcessItemPlanDTO(Integer id, Integer version, Integer ordinal, Integer planId, 
			Integer itemId, String itemValue, AmountWithUnit numberUnits) {
		super(id, version, ordinal);
		this.planId = planId;
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.numberUnits = numberUnits;
	}
	
	public ProcessItemPlanDTO(ProcessItemPlan processItemPlan) {
		super(processItemPlan);
		if(processItemPlan.getPlan() != null)
			this.planId = processItemPlan.getPlan().getId();
		if(processItemPlan.getItem() != null)
			this.item = new BasicValueEntity<Item>(processItemPlan.getItem());
		if(processItemPlan.getNumberUnits() != null)
			this.numberUnits = processItemPlan.getNumberUnits().clone();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProcessItemPlan.class;
	}
	
	@Override
	public ProcessItemPlan fillEntity(Object entity) {
		ProcessItemPlan processItemPlan;
		if(entity instanceof ProcessItemPlan) {
			processItemPlan = (ProcessItemPlan) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProcessItemPlan class");
		}
		super.fillEntity(processItemPlan);
		
		if(getItem() != null) {
			Item item = new Item();
			item.setId(getItem().getId());
			processItemPlan.setItem(item);
		}
		if (getNumberUnits() != null) {
			processItemPlan.setNumberUnits(getNumberUnits().clone());
		}	
		
		return processItemPlan;
	}

	
}
