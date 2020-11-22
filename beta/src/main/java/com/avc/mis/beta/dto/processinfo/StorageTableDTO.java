/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.List;

import com.avc.mis.beta.dto.process.inventory.BasicStorageDTO;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class StorageTableDTO {
	
	private List<BasicStorageDTO> amounts;

	private BigDecimal containerWeight;	
	
	private Warehouse warehouseLocation;

	
}
