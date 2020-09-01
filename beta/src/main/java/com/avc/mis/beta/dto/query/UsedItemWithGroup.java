/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UsedItemWithGroup  extends ValueDTO {
	
	UsedItemsGroupDTO usedItemsGroup;
	UsedItemDTO usedItem;
	
	public UsedItemWithGroup(@NonNull Integer id, Integer version, String groupName, boolean tableView,
			Integer usedId, Integer usedVersion, BigDecimal numberUnits,
			Integer itemId, String itemValue, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal storageNumberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id);
		this.usedItemsGroup = new UsedItemsGroupDTO(id, version, groupName, tableView);
		this.usedItem = new UsedItemDTO(usedId, usedVersion, numberUnits, itemId, itemValue, 
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName, 
				storageId, stoageVersion, storageOrdinal,
				unitAmount, measureUnit, storageNumberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, storageRemarks);
	}
	
	
	public UsedItemWithGroup(@NonNull Integer id, UsedItemsGroupDTO usedItemsGroup, UsedItemDTO usedItem) {
		super(id);
		this.usedItemsGroup = usedItemsGroup;
		this.usedItem = usedItem;
	}
	
	

}
