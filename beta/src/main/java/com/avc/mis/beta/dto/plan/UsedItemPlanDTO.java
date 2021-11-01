/**
 * 
 */
package com.avc.mis.beta.dto.plan;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.reference.BasicDataEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.plan.UsedItemPlan;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
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
public class UsedItemPlanDTO extends RankedAuditedDTO {

	@EqualsAndHashCode.Exclude
	private Integer planId;
	private BasicDataEntity<ProcessItem> processItem;
	private AmountWithUnit numberUnits;
	
	public UsedItemPlanDTO(Integer id, Integer version, Integer ordinal, Integer planId, 
			Integer processItemId, Integer processItemVersion, AmountWithUnit numberUnits) {
		super(id, version, ordinal);
		this.planId = planId;
		this.processItem = new BasicDataEntity<ProcessItem>(processItemId, processItemVersion);
		this.numberUnits = numberUnits;
	}
	
	public UsedItemPlanDTO(UsedItemPlan usedItemPlan) {
		super(usedItemPlan);
		if(usedItemPlan.getPlan() != null)
			this.planId = usedItemPlan.getPlan().getId();
		if(usedItemPlan.getProcessItem() != null) {
			this.processItem = new BasicDataEntity<ProcessItem>(usedItemPlan.getProcessItem());
		}
		if(usedItemPlan.getNumberUnits() != null)
			this.numberUnits = usedItemPlan.getNumberUnits().clone();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return UsedItemPlan.class;
	}
	
	@Override
	public UsedItemPlan fillEntity(Object entity) {
		UsedItemPlan usedItemPlan;
		if(entity instanceof UsedItemPlan) {
			usedItemPlan = (UsedItemPlan) entity;
		}
		else {
			throw new IllegalStateException("Param has to be UsedItemPlan class");
		}
		super.fillEntity(usedItemPlan);
		
		if(getProcessItem() != null) {
			ProcessItem processItem = new ProcessItem();
			processItem.setId(getProcessItem().getId());
			processItem.setVersion(getProcessItem().getVersion());
			usedItemPlan.setProcessItem(processItem);
		}
		if (getNumberUnits() != null) {
			usedItemPlan.setNumberUnits(getNumberUnits().clone());
		}	
		
		return usedItemPlan;
	}
}
