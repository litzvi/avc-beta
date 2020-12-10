/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.values.Warehouse;

/**
 * @author zvi
 *
 */
public interface StorageBaseDTO {

	public BigDecimal getUnitAmount();
	
	public BigDecimal getNumberUnits();
	
	public BigDecimal getAccessWeight();
	
	public BasicValueEntity<Warehouse> getWarehouseLocation();
	
	public String getClassName();

}
