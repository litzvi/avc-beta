/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnit;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.PackedItem;

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
	
	private ItemWithUnit item;
	
	private AmountWithUnit totalAmount;
	private AmountWithUnit totalWeight;
	private List<ProcessItemInventoryRow> poInventoryRows;

	public ItemInventoryRow(@NonNull ItemWithUnit item) {
		super(item.getId());
		this.item = item;
	}
	
	public void setPoInventoryRows(List<ProcessItemInventoryRow> poInventoryRows) {
		this.poInventoryRows = poInventoryRows;
		this.totalWeight = ProcessItemInventoryRow.getTotalWeight(poInventoryRows);
		
		if(item.getClazz() == BulkItem.class) {
			this.totalAmount = null;
		}
		else if(item.getClazz() == PackedItem.class){
			this.totalAmount = ProcessItemInventoryRow.getTotalAmount(poInventoryRows);
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
	}
	
	public List<AmountWithUnit> getTotalStock() {
		List<AmountWithUnit> totalStock = new ArrayList<>();
		if(this.totalAmount != null) {
			totalStock.add(this.totalAmount);
		}
		if(this.totalWeight != null) {
			totalStock.addAll(AmountWithUnit.weightDisplay(this.totalWeight, Arrays.asList(MeasureUnit.LBS, MeasureUnit.LOT)));
		}
		return totalStock;
	}
	
}
