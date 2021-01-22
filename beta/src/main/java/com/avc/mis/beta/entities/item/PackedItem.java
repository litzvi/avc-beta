/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

/**
 * @author zvi
 *
 */
@Entity
@DiscriminatorValue("packed")
public class PackedItem extends Item {

	public PackedItem() {
		super();
		super.setMeasureUnit(MeasureUnit.UNIT);
	}
	
	@Override
	public void setMeasureUnit(MeasureUnit measureUnit) {
		
		if(MeasureUnit.DISCRETE_UNITS.contains(measureUnit)) {
			super.setMeasureUnit(measureUnit);
		}
		else if(measureUnit != null) {
			throw new IllegalArgumentException("Packed item can't have a weight as default measure unit");			
		}
	}
	
	@Override
	public void setUnit(AmountWithUnit unit) {
		super.setUnit(unit);
	}
}
