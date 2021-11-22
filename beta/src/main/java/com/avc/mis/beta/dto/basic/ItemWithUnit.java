/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import java.math.BigDecimal;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ItemWithUnit extends BasicValueEntity<Item> {

	MeasureUnit measureUnit;
	AmountWithUnit unit;
	Class<? extends Item> clazz;
	
	public ItemWithUnit(Integer id, String value, MeasureUnit measureUnit, 
			BigDecimal unitAmount, MeasureUnit unitMeasureUnit, Class<? extends Item> clazz) {
		super(id, value);
		this.measureUnit = measureUnit;
		this.unit = new AmountWithUnit(unitAmount, unitMeasureUnit);
		this.clazz = clazz;
	}

}
