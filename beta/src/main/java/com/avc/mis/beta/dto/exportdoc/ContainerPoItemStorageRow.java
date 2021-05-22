/**
 * 
 */
package com.avc.mis.beta.dto.exportdoc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.basic.PoCodeBasic;
import com.avc.mis.beta.dto.values.ItemWithUnit;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
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
	String[] poCodes;
	AmountWithUnit unitAmount;
	BigDecimal numberUnits;	

	public ContainerPoItemStorageRow(
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz,  
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName,  
			String poCodes,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits) {
		super();
		this.itemWithUnit = new ItemWithUnit(itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz);
		if(poCodeId != null)
			this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		else
			this.poCode = null;
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		else
			this.poCodes = null;
		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);
		this.numberUnits = numberUnits;
		
	}
	
	public String getItem() {
		return itemWithUnit.getValue();
	}
	
	@JsonIgnore
	public AmountWithUnit getTotalWeight() {
		AmountWithUnit totalAmount;
		Class<? extends Item> itemClass = itemWithUnit.getClazz();
		if(MeasureUnit.NONE == itemWithUnit.getUnit().getMeasureUnit()) {
			totalAmount = unitAmount.multiply(numberUnits);
		}
		else if(MeasureUnit.WEIGHT_UNITS.contains(itemWithUnit.getUnit().getMeasureUnit())) {
			totalAmount = itemWithUnit.getUnit().multiply(unitAmount.getAmount().multiply(numberUnits));
		}
		else 
		{
			throw new IllegalStateException("The class can only apply to weight items");
		}
		return totalAmount;
	}
}
