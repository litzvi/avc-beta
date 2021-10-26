/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.process.inventory.StorageBaseDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Data;

/**
 * Used as a buffer to query,
 * process item with storage lists in one select constructor jpql query.
 * 
 * @author Zvi
 *
 */
@Data
public class ProcessItemWithStorage implements CollectionItemWithGroup<StorageDTO, ProcessItemDTO> {
	
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

	@Override
	public StorageDTO getItem() {
		return getStorage();
	}

	@Override
	public ProcessItemDTO getGroup() {
		return getProcessItem();
	}
	
}
