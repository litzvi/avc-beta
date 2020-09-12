/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.processinfo.LoadedItemDTO;
import com.avc.mis.beta.dto.processinfo.StorageDTO;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class LoadedItemWithStorage extends ValueDTO {

	private LoadedItemDTO loadedItem;
	private PoCodeDTO po;
	private StorageDTO storage;
	
	public LoadedItemWithStorage(Integer id, Integer version, 
			Integer itemId, String itemValue, ItemCategory itemCategory,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			Integer storageId, Integer storageVersion, Integer ordinal,
			BigDecimal unitAmount, MeasureUnit measureUnit,
			BigDecimal numberUnits, BigDecimal containerWeight, Integer warehouseLocationId,
			String warehouseLocationValue, String storageRemarks, Class<? extends Storage> clazz, 
			String groupName, String description, String remarks, boolean tableView,
			Integer itemPoCodeId, String itemContractTypeCode, String itemContractTypeSuffix, String itemSupplierName) {
		super(id);
		this.loadedItem = new LoadedItemDTO(id, version, 
				itemId, itemValue, itemCategory,
				groupName, description, remarks, tableView,
				itemPoCodeId, itemContractTypeCode, itemContractTypeSuffix, itemSupplierName);
		this.po = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.storage = new StorageDTO(storageId, storageVersion, ordinal, 
				unitAmount, measureUnit, numberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, storageRemarks, clazz);
	}

}
