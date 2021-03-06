/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.List;
import java.util.Optional;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
public class ItemInventoryAmountWithOrder {
	
	private BasicValueEntity<Item> item;

//	private AmountWithUnit[] inventoryWeight;
	private AmountWithUnit inventoryAmount;

//	private AmountWithUnit[] orderedWeight;
	private AmountWithUnit orderedAmount;
	
	public ItemInventoryAmountWithOrder(@NonNull BasicValueEntity<Item> item) {
		super();
		this.item = item;
	}
	
	public void setInventory(List<ItemAmount> inventoryAmounts) {
		if(inventoryAmounts == null || inventoryAmounts.isEmpty()) {
			this.inventoryAmount = null;
		}
		else {
			this.inventoryAmount = inventoryAmounts.stream()
					.map(i -> (AmountWithUnit)Optional.ofNullable(i.getAmount()).orElse(i.getWeightAmount()))
					.reduce(AmountWithUnit::add).get()
					.setScale(MeasureUnit.SCALE);	
		}
	}
	
	public void setOrder(List<ItemAmount> orderedAmounts) {
		if(orderedAmounts == null || orderedAmounts.isEmpty()) {
			this.orderedAmount = null;
		}
		else {
			this.orderedAmount = orderedAmounts.stream()
					.map(i -> (AmountWithUnit)Optional.ofNullable(i.getAmount()).orElse(i.getWeightAmount()))
					.reduce(AmountWithUnit::add).get()	
					.setScale(MeasureUnit.SCALE);	
		}
	}

	
	
}
