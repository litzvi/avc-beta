/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.InventoryUse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for Inventory Use processes, that record a use of items in inventory that aren't part of a production process.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class InventoryUseDTO extends RelocationProcessDTO {
		
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return InventoryUse.class;
	}
	
	@Override
	public InventoryUse fillEntity(Object entity) {
		InventoryUse inventoryUse;
		if(entity instanceof InventoryUse) {
			inventoryUse = (InventoryUse) entity;
		}
		else {
			throw new IllegalStateException("Param has to be InventoryUse class");
		}
		super.fillEntity(inventoryUse);
				
		return inventoryUse;
	}

}
