/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.process.group.ProcessItemDTO;
import com.avc.mis.beta.dto.process.storages.StorageDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;

/**
 * Used as a buffer to query,
 * process item with storage lists in one select constructor jpql query.
 * 
 * @author Zvi
 *
 */
@Data
public class ProcessItemWithStorage {
	
	private ProcessItemDTO processItem;

	private StorageDTO storage;
	
	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor, 
	 * that fetches process item, process and storage details.
	 */
	public ProcessItemWithStorage(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, AmountWithUnit itemUnit, Class<? extends Item> ItemClazz,
			MeasureUnit measureUnit, 
			Integer storageId, Integer storageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks, 
			Class<? extends Storage> clazz,
			String groupName, String description, String remarks, boolean tableView) {
		this.processItem = new ProcessItemDTO(id, version, ordinal,
				itemId, itemValue, productionUse, itemUnit, ItemClazz, 
				measureUnit, groupName, description, remarks, tableView);
		this.storage = new StorageDTO(storageId, storageVersion, storageOrdinal,
				unitAmount, numberUnits,
				warehouseLocationId, warehouseLocationValue, storageRemarks, clazz);
		
	}
	
}
