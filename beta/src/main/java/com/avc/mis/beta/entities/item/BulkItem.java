/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.enums.MeasureUnit;

/**
 * @author zvi
 *
 */
@Entity
//@Table(name = "ITEMS")
@DiscriminatorValue("bulk")
public class BulkItem extends Item {

	@Override
	public void setDefaultMeasureUnit(MeasureUnit measureUnit) {
		super.setDefaultMeasureUnit(measureUnit);
	}
}
