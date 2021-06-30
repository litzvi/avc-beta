/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.util.List;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class InventoryReportLine {

	private List<ItemAmount> inventory;

	private AmountWithUnit totalInventory;
	
	private List<ItemAmount> inventoryUse;
	private AmountWithUnit totalInventoryUse;


	public void setInventory(List<ItemAmount> inventory) {
		boolean empty = inventory == null || inventory.isEmpty();
		this.inventory = empty ? null : inventory;
		this.totalInventory = empty ? null : ItemAmount.getTotalWeight(inventory);

	}
	
	public void setInventoryUse(List<ItemAmount> inventoryUse) {
		boolean empty = inventoryUse == null || inventoryUse.isEmpty();
		this.inventoryUse = empty ? null : inventoryUse;
		this.totalInventoryUse = empty ? null : ItemAmount.getTotalWeight(inventoryUse);
	}

}
