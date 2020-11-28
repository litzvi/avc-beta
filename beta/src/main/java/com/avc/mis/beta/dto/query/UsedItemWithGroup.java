/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Data;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UsedItemWithGroup implements CollectionItemWithGroup<UsedItemDTO, UsedItemsGroupDTO> {
	
	UsedItemsGroupDTO usedItemsGroup;
	UsedItemDTO usedItem;
	
	public UsedItemWithGroup(@NonNull Integer id, Integer version,  Integer ordinal, String groupName, boolean tableView,
			Integer usedId, Integer usedVersion, Integer usedOrdinal, BigDecimal numberUnits,
			Integer itemId, String itemValue, MeasureUnit measureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal storageNumberUnits, BigDecimal otherUsedUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
//		super(id);
		this.usedItemsGroup = new UsedItemsGroupDTO(id, version, ordinal, groupName, tableView);
		this.usedItem = new UsedItemDTO(usedId, usedVersion, usedOrdinal, numberUnits, 
				itemId, itemValue, measureUnit, itemProcessDate,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal,
				unitAmount, storageNumberUnits, otherUsedUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, storageRemarks);
	}
	
	
//	public UsedItemWithGroup(@NonNull Integer id, UsedItemsGroupDTO usedItemsGroup, UsedItemDTO usedItem) {
//		super(id);
//		this.usedItemsGroup = usedItemsGroup;
//		this.usedItem = usedItem;
//	}


	@Override
	public UsedItemDTO getItem() {
		return getUsedItem();
	}


	@Override
	public UsedItemsGroupDTO getGroup() {
		return getUsedItemsGroup();
	}
	
	

}
