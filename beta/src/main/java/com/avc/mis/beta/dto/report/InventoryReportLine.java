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


	public void setInventory(List<ItemAmount> inventory) {
		boolean empty = inventory == null || inventory.isEmpty();
		this.inventory = empty ? null : inventory;
		this.totalInventory = empty ? null : ItemAmount.getTotalWeight(inventory);

	}

}
