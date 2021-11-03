/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.InventoryUse;
import com.avc.mis.beta.entities.process.RelocationProcess;
import com.avc.mis.beta.entities.process.collection.StorageMovesGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class InventoryUseDTO extends RelocationProcessDTO {
	
	public InventoryUseDTO(InventoryUse inventoryUse) {
		super(inventoryUse);
	}
	
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
	
	@Override
	public String getProcessTypeDescription() {
		return "Inventory Use";
	}

}
