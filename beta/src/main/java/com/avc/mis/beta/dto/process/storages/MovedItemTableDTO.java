/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * List of moved items in table format.
 * Used when using storages inserted as a storage table, therefore belong to the same process item.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MovedItemTableDTO extends UsedItemTableDTO {
	
	private BasicValueEntity<Warehouse> newWarehouseLocation;

}
