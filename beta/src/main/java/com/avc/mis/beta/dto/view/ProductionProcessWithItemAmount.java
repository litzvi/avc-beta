/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProductionProcessWithItemAmount extends BasicDTO {

	private BasicValueEntity<Item> item;
	private AmountWithUnit weight;
	private AmountWithUnit amount;
	private String[] warehouses;
	
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
		if(clazz == BulkItem.class) {
			this.amount = null;
			this.weight = new AmountWithUnit(amount, defaultMeasureUnit);
		}
		else if(clazz == PackedItem.class){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.weight = new AmountWithUnit(amount.multiply(unitAmount), unitMeasureUnit);
		}
		else 
		{
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);	
			this.weight = null;
		}
//		{
//			throw new IllegalStateException("The class can only apply to weight items");
//		}
				
	}
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			BasicValueEntity<Item> item, AmountWithUnit amount, AmountWithUnit weight, 
			String warehouses) {
		super(id);
		this.item = item;
		this.amount = amount;
		this.weight = weight;
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
	}
	
	public List<AmountWithUnit> getAmountList() {
		List<AmountWithUnit> amountList = new ArrayList<>();
		if(this.amount != null) {
			amountList.add(this.amount);
		}
		if(this.weight != null) {
			amountList.addAll(AmountWithUnit.weightDisplay(this.weight, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS)));
		}
		return amountList;

	}
	
	public static Optional<AmountWithUnit> getWeightSum(List<ProductionProcessWithItemAmount> items) {
		return items.stream()
				.filter(i -> i.getWeight() != null)
				.map(i -> i.getWeight())
				.reduce(AmountWithUnit::add);
	}
	
}
