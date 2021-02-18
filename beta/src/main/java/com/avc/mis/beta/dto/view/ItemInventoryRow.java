/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * DTO of inventory for one item. 
 * Contains total stock in inventory for this item, it's storages
 * and information of the originating process, process item and used amounts.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemInventoryRow extends BasicDTO {
	
	private ItemDTO item;
	
	private AmountWithUnit[] totalStock;	
	private List<ProcessItemInventoryRow> poInventoryRows;

	public ItemInventoryRow(@NonNull ItemDTO item) {
		super(item.getId());
		this.item = item;
	}
	
	public void setPoInventoryRows(List<ProcessItemInventoryRow> poInventoryRows) {
		this.poInventoryRows = poInventoryRows;
		this.totalStock = ProcessItemInventoryRow.getTotalStock(poInventoryRows);
	}
	
}
