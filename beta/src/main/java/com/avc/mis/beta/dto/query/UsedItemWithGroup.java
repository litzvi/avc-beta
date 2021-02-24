/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemsGroupDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
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
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit measureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers, 
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal storageNumberUnits, BigDecimal otherUsedUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
//		super(id);
		this.usedItemsGroup = new UsedItemsGroupDTO(id, version, ordinal, groupName, tableView);
		this.usedItem = new UsedItemDTO(usedId, usedVersion, usedOrdinal, numberUnits, 
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				measureUnit, itemProcessDate,
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, 
				itemPoCodes, itemSuppliers,
				storageId, stoageVersion, storageOrdinal,
				unitAmount, storageNumberUnits, otherUsedUnits, //accessWeight,
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
