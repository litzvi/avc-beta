/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemWithUnitDTO extends ValueDTO {

	String value;
	MeasureUnit defaultMeasureUnit;
	ItemGroup group;
	ProductionUse productionUse;
	AmountWithUnit unit;
	Class<? extends Item> clazz;
	
	public ItemWithUnitDTO(Integer id, String value, MeasureUnit defaultMeasureUnit, 
			ItemGroup group, ProductionUse productionUse, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz) {
		super(id);
		this.value = value;
		this.defaultMeasureUnit = defaultMeasureUnit;
		this.group = group;
		this.productionUse = productionUse;
		this.unit = new AmountWithUnit(unitAmount, unitMeasureUnit);
		this.clazz = clazz;
	}
	
	public ItemWithUnitDTO(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
		this.defaultMeasureUnit = item.getDefaultMeasureUnit();
		this.group = item.getItemGroup();
		this.productionUse = item.getProductionUse();
		this.unit = item.getUnit();
		this.clazz = item.getClass();
	}

}
