/**
 * 
 */
package com.avc.mis.beta.dto.doc;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.ItemWithUnit;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.PackedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ContainerPoItemStorageRow {

	@JsonIgnore
	ItemWithUnit itemWithUnit;
	PoCodeBasic poCode;
	AmountWithUnit unitAmount;
//	BigDecimal containerWeight;
	BigDecimal numberUnits;	

	public ContainerPoItemStorageRow(
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz,  
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits) {
		super();
		this.itemWithUnit = new ItemWithUnit(itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);
//		this.containerWeight = containerWeight;
		this.numberUnits = numberUnits;
		
	}
	
	public String getItem() {
		return itemWithUnit.getValue();
	}
	
	@JsonIgnore
	public AmountWithUnit getTotalWeight() {
		AmountWithUnit totalAmount;
		Class<? extends Item> itemClass = itemWithUnit.getClazz();
		if(itemClass == BulkItem.class) {
			totalAmount = unitAmount.multiply(numberUnits);
		}
		else if(itemClass == PackedItem.class){
			totalAmount = itemWithUnit.getUnit().multiply(unitAmount.getAmount().multiply(numberUnits));
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
		return totalAmount;
	}
}
