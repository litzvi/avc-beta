/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.util.List;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;

/**
 * List of storages in table format, where all share the same warehouse.
 * 
 * @author zvi
 *
 */
@Data
public class StorageTableDTO {
	
	private List<BasicStorageDTO> amounts;

//	private BigDecimal accessWeight;	
	
	private BasicValueEntity<Warehouse> warehouseLocation;

	
}
