/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.values.Item;

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
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemInventoryRow extends ValueDTO {
	
	ItemDTO item;
	AmountWithUnit totalStock;
	
	List<ProcessItemInventoryRow> poInventoryRows;

	public ItemInventoryRow(@NonNull ItemDTO item, AmountWithUnit totalStock,
			List<ProcessItemInventoryRow> poInventoryRows) {
		super(item.getId());
		this.item = item;
		this.totalStock = totalStock;
		this.poInventoryRows = poInventoryRows;
	}
	

	
}
