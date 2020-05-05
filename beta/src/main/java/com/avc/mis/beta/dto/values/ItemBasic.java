/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.values.Item;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ItemBasic extends ValueDTO {
	
	String value;

	public ItemBasic(Integer id, String value) {
		super(id);
		this.value = value;
	}
	
	public ItemBasic(@NonNull Item item) {
		super(item.getId());
		this.value = item.getValue();
	}
}
