/**
 * 
 */
package com.avc.mis.beta.entities.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

/**
 * @author zvi
 *
 */
@Entity
//@Table(name = "ITEMS")
@DiscriminatorValue("packed")
public class PackedItem extends Item {

	@Override
	public void setUnit(AmountWithUnit unit) {
		super.setUnit(unit);
	}
}
