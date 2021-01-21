/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.BulkItem;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.PackedItem;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProductionProcessWithItemAmount extends BasicDTO {

	BasicValueEntity<Item> item;
	AmountWithUnit[] weight;
	AmountWithUnit amount;
	@NonFinal
	String[] warehouses;
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz,
			BigDecimal amount, String warehouses) {
		this(id, itemId, itemValue, defaultMeasureUnit, unitAmount, unitMeasureUnit, clazz, amount);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
		
	}
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz,
			BigDecimal amount) {
		super(id);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		AmountWithUnit amountWithUnit;
		if(clazz == BulkItem.class) {
			this.amount = null;
			amountWithUnit = new AmountWithUnit(amount, defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			amountWithUnit = new AmountWithUnit(amount.multiply(unitAmount), unitMeasureUnit);
		}
		else 
		{
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);	
			amountWithUnit = null;
		}
//		{
//			throw new IllegalStateException("The class can only apply to weight items");
//		}
		
		if(amountWithUnit != null) {
			this.weight = new AmountWithUnit[] {
					amountWithUnit.convert(MeasureUnit.KG),
					amountWithUnit.convert(MeasureUnit.LBS)};
			AmountWithUnit.setScales(this.weight, MeasureUnit.SCALE);
		}
		else {
			this.weight = null;
		}
				
	}
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			BasicValueEntity<Item> item, AmountWithUnit weight, AmountWithUnit amount,
			String warehouses) {
		super(id);
		this.item = item;
		if(weight != null) {
			this.weight = new AmountWithUnit[] {
					weight.convert(MeasureUnit.KG),
					weight.convert(MeasureUnit.LBS)};
			AmountWithUnit.setScales(this.weight, MeasureUnit.SCALE);
		}
		else {			
			this.weight = null;
		}
		this.amount = amount;
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
	}
	
	public static Optional<AmountWithUnit> getWeightSum(List<ProductionProcessWithItemAmount> items) {
		return items.stream()
				.filter(i -> i.getWeight() != null)
				.map(i -> i.getWeight()[0])
				.reduce(AmountWithUnit::add);
	}
	
}
