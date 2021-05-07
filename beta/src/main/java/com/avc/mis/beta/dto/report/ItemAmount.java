/**
 * 
 */
package com.avc.mis.beta.dto.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	@JsonIgnore
	AmountWithUnit weightAmount;
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
		if(clazz == BulkItem.class && MeasureUnit.WEIGHT_UNITS.contains(defaultMeasureUnit)) {
			this.amount = null;
			this.weightAmount = new AmountWithUnit(amount.multiply(this.weightCoefficient, MathContext.DECIMAL64), defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class && MeasureUnit.WEIGHT_UNITS.contains(unitMeasureUnit)){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.amount.setScale(MeasureUnit.SCALE);
			AmountWithUnit weightAmount = new AmountWithUnit(
					amount
					.multiply(unitAmount, MathContext.DECIMAL64)
					.multiply(this.weightCoefficient, MathContext.DECIMAL64), 
					unitMeasureUnit);
			
			//not display oz, gram
			MeasureUnit systemMainMU = MeasureUnit.getSystemMainUnit(unitMeasureUnit);
			if(unitMeasureUnit != systemMainMU) {
				this.weightAmount = weightAmount.convert(systemMainMU);
			}
			else {
				this.weightAmount = weightAmount;
			}
//			this.weight = new AmountWithUnit[] {
//					weight.convert(MeasureUnit.LBS),
//					weight.convert(MeasureUnit.KG)};
//			AmountWithUnit.setScales(this.weight, MeasureUnit.SCALE);

		}
		else 
		{
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);	
			this.weightAmount = null;
//			throw new IllegalStateException("The class can only apply to weight items");
		}
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
	
	public List<AmountWithUnit> getWeight() {
		if(this.weightAmount != null) {
			return AmountWithUnit.weightDisplay(this.weightAmount, Arrays.asList(MeasureUnit.LBS, MeasureUnit.KG));	
		}
		else {
			return null;
		}
	}
	
	@JsonIgnore
	static AmountWithUnit getTotalWeight(List<ItemAmount> itemAmounts) {
		Optional<AmountWithUnit> totalWeight = itemAmounts.stream()
				.filter(i -> i.getWeightAmount() != null)
				.map(i -> i.getWeightAmount())
				.reduce(AmountWithUnit::add);
		if(totalWeight.isPresent()) {
			return totalWeight.get()
					.setScale(MeasureUnit.SUM_DISPLAY_SCALE);
		}
		else {
			return null;
		}
	}
	

}
