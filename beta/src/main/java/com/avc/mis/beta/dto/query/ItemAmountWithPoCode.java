/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;

import com.avc.mis.beta.dto.report.ItemAmount;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.codes.PoCode;
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
public class ItemAmountWithPoCode {

	PoCodeBasic poCode;
	BasicValueEntity<Item> item;

	@ToString.Exclude @JsonIgnore
	ItemGroup itemGroup;
	AmountWithUnit weightAmount;
	AmountWithUnit amount;
	
	public ItemAmountWithPoCode(
			Integer poCodeId, String poCodeCode, 
			String contractTypeCode, String contractTypeSuffix, String supplierName, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount) {
		super();
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.itemGroup = itemGroup;
		if(clazz == BulkItem.class) {
			this.amount = null;
			this.weightAmount = new AmountWithUnit(amount, defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.weightAmount = new AmountWithUnit(
					amount.multiply(unitAmount, MathContext.DECIMAL64), 
					unitMeasureUnit);
			this.amount.setScale(MeasureUnit.SCALE);
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
	}
		
	@JsonIgnore
	static AmountWithUnit getTotalWeight(List<ItemAmountWithPoCode> itemAmounts) {
		return itemAmounts.stream().map(i -> i.getWeightAmount()).reduce(AmountWithUnit::add).get();
	}
	
}