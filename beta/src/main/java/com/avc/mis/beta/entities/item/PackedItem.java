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
//@Table(name = "ITEMS")
@DiscriminatorValue("packed")
public class PackedItem extends Item {

	public PackedItem() {
		super();
		setDefaultMeasureUnit(MeasureUnit.UNIT);
	}
	
	@Override
	public void setUnit(AmountWithUnit unit) {
		super.setUnit(unit);
	}
}
