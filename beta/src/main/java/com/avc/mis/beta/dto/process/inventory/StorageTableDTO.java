/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.util.List;

import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;

/**
 * @author zvi
 *
 */
@Data
public class StorageTableDTO {
	
	private List<BasicStorageDTO> amounts;

//	private BigDecimal accessWeight;	
	
	private BasicValueEntity<Warehouse> warehouseLocation;

	
}
