/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.PackedItem;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
public class ItemAmount {

	BasicValueEntity<Item> item;

	@ToString.Exclude @JsonIgnore
	ItemGroup itemGroup;
	AmountWithUnit[] weight;
	AmountWithUnit amount;
	
	public ItemAmount(Integer id, String value, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount) {
		super();
		this.item = new BasicValueEntity<Item>(id, value);
		this.itemGroup = itemGroup;
		AmountWithUnit amountWithUnit;
		if(clazz == BulkItem.class) {
			this.amount = null;
			amountWithUnit = new AmountWithUnit(amount, defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.amount.setScale(MeasureUnit.SCALE);
			amountWithUnit = new AmountWithUnit(amount.multiply(unitAmount), unitMeasureUnit);
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
		this.weight = new AmountWithUnit[] {
				amountWithUnit.convert(MeasureUnit.LBS),
				amountWithUnit.convert(MeasureUnit.KG)};
		AmountWithUnit.setScales(this.weight, MeasureUnit.SCALE);
	}
	

}
