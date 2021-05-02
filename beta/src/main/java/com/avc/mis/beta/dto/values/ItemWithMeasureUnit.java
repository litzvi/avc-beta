/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ItemWithMeasureUnit extends BasicValueEntity<Item> {

	MeasureUnit measureUnit;
	
	public ItemWithMeasureUnit(Integer id, String value, MeasureUnit measureUnit) {
		super(id, value);
		this.measureUnit = measureUnit;
	}
	
	public ItemWithMeasureUnit(@NonNull Item item) {
		super(item);
		this.measureUnit = item.getMeasureUnit();
	}
	
//	public ItemWithMeasureUnit(Integer id, String value) {
//		super(id, value);
//	}

}
