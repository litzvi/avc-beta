/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.processinfo.ProcessItemDTO;
import com.avc.mis.beta.dto.processinfo.StorageDTO;
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
			Integer usedId, Integer usedVersion, Integer ordinal,
			Integer itemId, String itemValue, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			BigDecimal unitAmount, MeasureUnit measureUnit,
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal numberUnits, BigDecimal containerWeight) {
		super(id);
		this.usedItemsGroup = new UsedItemsGroupDTO(id, version, groupName, tableView);
		this.usedItem = new UsedItemDTO(usedId, usedVersion, ordinal, itemId, itemValue, 
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName, 
				unitAmount, measureUnit, warehouseLocationId, warehouseLocationValue, numberUnits, containerWeight);
	}
	
	
	public UsedItemWithGroup(@NonNull Integer id, UsedItemsGroupDTO usedItemsGroup, UsedItemDTO usedItem) {
		super(id);
		this.usedItemsGroup = usedItemsGroup;
		this.usedItem = usedItem;
	}
	
	

}
