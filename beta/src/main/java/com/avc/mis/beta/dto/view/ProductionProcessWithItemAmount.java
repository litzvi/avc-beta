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
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * Entry in list of used/produced/count in production or other detailed report with gain and loss for process.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProductionProcessWithItemAmount extends BasicDTO {

	@Getter(value = AccessLevel.NONE)
	private List<MeasureUnit> weightDisplayMeasureUnits = Arrays.asList(MeasureUnit.LBS);
	
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
			this.warehouses = Stream.of(warehouses.split(",")).toArray(String[]::new);
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
		if(MeasureUnit.NONE == unitMeasureUnit && MeasureUnit.WEIGHT_UNITS.contains(defaultMeasureUnit)) {
			this.amount = null;
			this.weight = new AmountWithUnit(amount, defaultMeasureUnit);
		}
		else if(MeasureUnit.WEIGHT_UNITS.contains(unitMeasureUnit)){
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);
			this.weight = new AmountWithUnit(amount.multiply(unitAmount), unitMeasureUnit).setScale(MeasureUnit.SUM_DISPLAY_SCALE);
		}
		else 
		{
			this.amount = new AmountWithUnit(amount, defaultMeasureUnit);	
			this.weight = null;
		}
				
	}
		
	public List<AmountWithUnit> getAmountList() {
		List<AmountWithUnit> amountList = new ArrayList<>();
		if(this.amount != null) {
			amountList.add(this.amount);
		}
		if(this.weight != null) {
			amountList.addAll(AmountWithUnit.weightDisplay(this.weight, weightDisplayMeasureUnits));
//			if(this.weight.getMeasureUnit() != DISPLAY_MEASURE_UNIT) {
//				amountList.add(this.weight);
//			}				
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
