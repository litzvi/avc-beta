/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;

import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.Storage;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PoInventoryRowWithStorage extends ProcessItemWithStorage {

	
	public PoInventoryRowWithStorage(Integer id, Integer version, Integer itemId, String itemValue, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, 
			Integer storageId, Integer storageVersion,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId, String warehouseLocationValue, 
			String description, String remarks, Class<? extends Storage> clazz) {
		super(id, version, itemId, itemValue, 
				poCodeId, contractTypeCode, supplierName, 
				storageId, storageVersion, 
				unitAmount, measureUnit, numberUnits, 
				warehouseLocationId, warehouseLocationValue, 
				description, remarks, clazz);
	}
	
	
}
