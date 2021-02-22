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
@DiscriminatorValue("bulk")
public class BulkItem extends Item {

	public BulkItem() {
		super();
		super.setUnit(AmountWithUnit.NEUTRAL);
	}
	
	public BulkItem(MeasureUnit measureUnit) {
		super();
		super.setUnit(AmountWithUnit.NEUTRAL);
		super.setMeasureUnit(measureUnit);
	}
		
	@Override
	public void setUnit(AmountWithUnit unit) {
	}
}
