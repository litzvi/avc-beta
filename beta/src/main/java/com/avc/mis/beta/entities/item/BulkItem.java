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
@DiscriminatorValue("bulk")
public class BulkItem extends Item {

	public BulkItem() {
		super();
		setUnit(AmountWithUnit.ONE_UNIT);
	}
	
	@Override
	public void setDefaultMeasureUnit(MeasureUnit measureUnit) {
		super.setDefaultMeasureUnit(measureUnit);
	}
}
