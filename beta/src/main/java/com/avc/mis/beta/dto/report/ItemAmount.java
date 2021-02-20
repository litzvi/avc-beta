/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import com.avc.mis.beta.dto.values.BasicValueEntity;
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
	@JsonIgnore
	BigDecimal weightCoefficient;
	
	public ItemAmount(Integer id, String value, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount, BigDecimal weightCoefficient) {
		super();
		this.item = new BasicValueEntity<Item>(id, value);
		this.itemGroup = itemGroup;
		if(weightCoefficient != null) {
			this.weightCoefficient = weightCoefficient;
		}
		else {
			this.weightCoefficient = BigDecimal.ONE;
		}
		AmountWithUnit weight;
		if(clazz == BulkItem.class) {
			this.amount = null;
			weight = new AmountWithUnit(amount.multiply(this.weightCoefficient, MathContext.DECIMAL64), defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.amount.setScale(MeasureUnit.SCALE);
			weight = new AmountWithUnit(
					amount
					.multiply(unitAmount, MathContext.DECIMAL64)
					.multiply(this.weightCoefficient, MathContext.DECIMAL64), 
					unitMeasureUnit);
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
		this.weight = new AmountWithUnit[] {
				weight.convert(MeasureUnit.LBS),
				weight.convert(MeasureUnit.KG)};
		AmountWithUnit.setScales(this.weight, MeasureUnit.SCALE);
	}
	
	public ItemAmount(Integer id, String value, MeasureUnit defaultMeasureUnit, 
			ItemGroup itemGroup, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz, 
			BigDecimal amount) {
		this(id, value, defaultMeasureUnit, 
				itemGroup, productionUse, 
				unitAmount, unitMeasureUnit, clazz, 
				amount, BigDecimal.ONE);
	}
	
	@JsonIgnore
	static AmountWithUnit getTotalWeight(List<ItemAmount> itemAmounts) {
		return itemAmounts.stream().map(i -> i.getWeight()[0]).reduce(AmountWithUnit::add).get();
	}
	

}
