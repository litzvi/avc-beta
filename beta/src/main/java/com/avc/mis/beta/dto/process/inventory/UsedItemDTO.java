package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.inventory.UsedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UsedItemDTO extends UsedItemBaseDTO {
	
	public UsedItemDTO(Integer id, Integer version, Integer ordinal, BigDecimal numberUsedUnits,
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit measureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal storageNumberUnits, BigDecimal otherUsedUnits, BigDecimal containerWeight, 
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id, version, ordinal, numberUsedUnits,
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				measureUnit, itemProcessDate,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal,
				unitAmount, storageNumberUnits, otherUsedUnits, containerWeight,
				warehouseLocationId,  warehouseLocationValue, storageRemarks);	
	}

	public UsedItemDTO(UsedItem usedItem) {
		super(usedItem);
	}

	
}
