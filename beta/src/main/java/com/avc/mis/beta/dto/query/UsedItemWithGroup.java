/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.process.group.UsedItemsGroupDTO;
import com.avc.mis.beta.dto.process.storages.UsedItemDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.NonNull;

/**
 * Used for fetching by query of used items with their group.
 * 
 * @author zvi
 *
 */
@Data
public class UsedItemWithGroup {
	
	UsedItemsGroupDTO usedItemsGroup;
	UsedItemDTO usedItem;
	
	public UsedItemWithGroup(@NonNull Integer id, Integer version,  Integer ordinal, String groupName, boolean tableView,
			Integer usedId, Integer usedVersion, Integer usedOrdinal, BigDecimal numberUnits,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit measureUnit, LocalDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers, 
			Integer gradeId,  String gradeValue,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal storageNumberUnits, BigDecimal otherUsedUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		this.usedItemsGroup = new UsedItemsGroupDTO(id, version, ordinal, groupName, tableView);
		this.usedItem = new UsedItemDTO(usedId, usedVersion, usedOrdinal, numberUnits, 
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				measureUnit, itemProcessDate,
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, 
				itemPoCodes, itemSuppliers, 
				gradeId, gradeValue,
				storageId, stoageVersion, storageOrdinal,
				unitAmount, storageNumberUnits, otherUsedUnits, //accessWeight,
				warehouseLocationId, warehouseLocationValue, storageRemarks);
	}

}
