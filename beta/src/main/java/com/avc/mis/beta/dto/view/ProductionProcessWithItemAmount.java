/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ProductionProcessWithItemAmount extends ValueDTO {

	BasicValueEntity<Item> item;
	AmountWithUnit[] amountWithUnit;
	@NonFinal
	String[] warehouses;
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			Integer itemId, String itemValue, 
			BigDecimal amount, MeasureUnit measureUnit,
			String warehouses) {
		this(id, itemId, itemValue, amount, measureUnit);
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
		
	}
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			Integer itemId, String itemValue, 
			BigDecimal amount, MeasureUnit measureUnit) {
		super(id);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		AmountWithUnit amountWithUnit = new AmountWithUnit(amount, measureUnit);
		this.amountWithUnit = new AmountWithUnit[] {
				amountWithUnit.convert(MeasureUnit.KG),
				amountWithUnit.convert(MeasureUnit.LBS)};
		AmountWithUnit.setScales(this.amountWithUnit, MeasureUnit.SCALE);
				
	}
	
	public ProductionProcessWithItemAmount(@NonNull Integer id, 
			BasicValueEntity<Item> item, AmountWithUnit amountWithUnit,
			String warehouses) {
		super(id);
		this.item = item;
		this.amountWithUnit = new AmountWithUnit[] {
				amountWithUnit.convert(MeasureUnit.KG),
				amountWithUnit.convert(MeasureUnit.LBS)};
		AmountWithUnit.setScales(this.amountWithUnit, MeasureUnit.SCALE);		
		if(warehouses != null) {
			this.warehouses = Stream.of(warehouses.split(",")).distinct().toArray(String[]::new);
		}
		else {
			this.warehouses = null;
		}
	}
	
	
	
}
