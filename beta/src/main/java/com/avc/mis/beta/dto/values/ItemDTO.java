/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ItemDTO extends ValueDTO {

	String value;
	MeasureUnit measureUnit;
	
	public ItemDTO(Integer id, String value, MeasureUnit measureUnit) {
		super(id);
		this.value = value;
		this.measureUnit = measureUnit;
	}
	
	public ItemDTO(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
		this.measureUnit = item.getMeasureUnit();
	}

}
