/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemInventoryRow extends ValueDTO {
	
	BasicValueEntity<Item> item;
	AmountWithUnit totalStock;
	
	List<ProcessItemInventoryRow> poInventoryRows;

	public ItemInventoryRow(@NonNull BasicValueEntity<Item> item, AmountWithUnit totalStock,
			List<ProcessItemInventoryRow> poInventoryRows) {
		super(item.getId());
		this.item = item;
		this.totalStock = totalStock;
		this.poInventoryRows = poInventoryRows;
	}
	

	
}
