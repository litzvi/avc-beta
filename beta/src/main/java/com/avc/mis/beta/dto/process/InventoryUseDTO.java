/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.entities.process.InventoryUse;

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
public class InventoryUseDTO extends TransactionProcessDTO<ProcessItemDTO> {
	
	public InventoryUseDTO(InventoryUse inventoryUse) {
		super(inventoryUse);
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Inventory Use";
	}

}
