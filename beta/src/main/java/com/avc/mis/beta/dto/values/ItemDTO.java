/**
 * 
 */
package com.avc.mis.beta.dto.values;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SupplyGroup;
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
	SupplyGroup supplyGroup;
	ItemCategory category;

	
	public ItemDTO(Integer id, String value, MeasureUnit measureUnit, 
			SupplyGroup supplyGroup, ItemCategory category) {
		super(id);
		this.value = value;
		this.measureUnit = measureUnit;
		this.supplyGroup = supplyGroup;
		this.category = category;
	}
	
	public ItemDTO(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
		this.measureUnit = item.getMeasureUnit();
		this.supplyGroup = item.getSupplyGroup();
		this.category = item.getCategory();
	}

}
