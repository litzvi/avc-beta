/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.inventory.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Used as a buffer to query,
 * process item with storage lists in one select constructor jpql query.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProcessItemWithStorage extends ValueDTO {
	
	private ProcessItemDTO processItem;
	private PoCodeDTO po;
	private StorageDTO storage;
	
	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor, 
	 * that fetches process item, process and storage details.
	 */
	public ProcessItemWithStorage(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> ItemClazz,
			MeasureUnit measureUnit, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer storageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks, 
			Class<? extends Storage> clazz,
			String groupName, String description, String remarks, boolean tableView) {
		super(id);
		this.processItem = new ProcessItemDTO(id, version, ordinal,
				itemId, itemValue, productionUse, ItemClazz, measureUnit, 
				groupName, description, remarks, tableView);
		this.po = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.storage = new StorageDTO(storageId, storageVersion, storageOrdinal,
				unitAmount, numberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, storageRemarks, clazz);
		
	}
	
}
