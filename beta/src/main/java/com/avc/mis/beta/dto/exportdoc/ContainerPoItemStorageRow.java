/**
 * 
 */
package com.avc.mis.beta.dto.exportdoc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.basic.ItemWithUnit;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;
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
	String[] poCodes;
	AmountWithUnit unitAmount;
	BigDecimal numberUnits;
	BigDecimal numberBoxes;

	public ContainerPoItemStorageRow(
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz,  
			String poCodes,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal numberBoxes) {
		super();
		this.itemWithUnit = new ItemWithUnit(itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz);
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		else
			this.poCodes = null;
		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);
		this.numberUnits = numberUnits;
		this.numberBoxes = numberBoxes;
	}
	
	public String getItem() {
		return itemWithUnit.getValue();
	}
	
	@JsonIgnore
	public AmountWithUnit getTotalWeight() {
		AmountWithUnit totalWeight;
		Class<? extends Item> itemClass = itemWithUnit.getClazz();
		if(MeasureUnit.NONE == itemWithUnit.getUnit().getMeasureUnit() && MeasureUnit.WEIGHT_UNITS.contains(unitAmount.getMeasureUnit())) {
			totalWeight = unitAmount.multiply(numberUnits);
		}
		else if(MeasureUnit.WEIGHT_UNITS.contains(itemWithUnit.getUnit().getMeasureUnit())) {
			totalWeight = itemWithUnit.getUnit().multiply(unitAmount.getAmount().multiply(numberUnits));
		}
		else 
		{
			return null;
		}
		return totalWeight;
	}
}
