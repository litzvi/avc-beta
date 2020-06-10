/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.StorageDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.Storage;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@ToString(callSuper = true)
public class PoInventoryRowWithStorage{

	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	StorageDTO storage;
	
	public PoInventoryRowWithStorage(
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, 
			String itemName,
			Integer storageId, Integer storageVersion,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks, 
			Class<? extends Storage> clazz) {
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.storage = new StorageDTO(storageId, storageVersion, 
				unitAmount, measureUnit, numberUnits, 
				warehouseLocationId, warehouseLocationValue, storageRemarks, clazz);
	}
	
	
}
