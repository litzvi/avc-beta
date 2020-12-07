/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.ValueInterface;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemWithUnit extends BasicValueEntity<Item> {

	MeasureUnit defaultMeasureUnit;
	AmountWithUnit unit;
	Class<? extends Item> clazz;
	
	public ItemWithUnit(Integer id, String value, MeasureUnit defaultMeasureUnit, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz) {
		super(id, value);
		this.defaultMeasureUnit = defaultMeasureUnit;
		this.unit = new AmountWithUnit(unitAmount, unitMeasureUnit);
		this.clazz = clazz;
	}
	
	public ItemWithUnit(@NonNull Item item) {
		super(item);
		this.defaultMeasureUnit = item.getDefaultMeasureUnit();
		this.unit = item.getUnit();
		this.clazz = item.getClass();
	}
	
	public ItemWithUnit(Integer id, String value) {
		super(id, value);
	}

}
