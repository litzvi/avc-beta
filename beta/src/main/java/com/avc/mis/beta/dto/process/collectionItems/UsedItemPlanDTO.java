/**
 * 
 */
package com.avc.mis.beta.dto.process.collectionItems;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.BasicDataEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.process.collectionItems.UsedItemPlan;
import com.avc.mis.beta.entities.process.group.ProcessItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Used item (input) of a planned production.
 * 
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
