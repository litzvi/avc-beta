/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.List;

import com.avc.mis.beta.dto.basic.ContainerArrivalBasic;
import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.values.ShipmentCodeDTO;
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
