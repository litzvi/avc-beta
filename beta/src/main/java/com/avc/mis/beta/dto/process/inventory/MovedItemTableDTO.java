/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MovedItemTableDTO extends UsedItemTableDTO {
	
	private BasicValueEntity<Warehouse> newWarehouseLocation;

}
