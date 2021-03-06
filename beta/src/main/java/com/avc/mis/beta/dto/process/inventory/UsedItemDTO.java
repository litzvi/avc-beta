package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
			MeasureUnit measureUnit, LocalDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers, 
			Integer gradeId,  String gradeValue,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal storageNumberUnits, BigDecimal storgeOtherUsedUnits, //BigDecimal accessWeight, 
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks) {
		super(id, version, ordinal, numberUsedUnits,
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				measureUnit, itemProcessDate,
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, 
				itemPoCodes, itemSuppliers, 
				gradeId, gradeValue,
				storageId, stoageVersion, storageOrdinal,
				unitAmount, storageNumberUnits, storgeOtherUsedUnits, //accessWeight,
				warehouseLocationId,  warehouseLocationValue, storageRemarks);	
	}

	public UsedItemDTO(UsedItem usedItem) {
		super(usedItem);
	}
	
	@JsonIgnore
	public BigDecimal getTotal() {
		if(getStorage() == null || getStorage().getUnitAmount() == null || getNumberUsedUnits() == null) {
			return null;
		}
		else {
			return getStorage()
				.getUnitAmount()
				.multiply(getNumberUsedUnits());
//				.subtract(Optional.ofNullable(getAccessWeight()).orElse(BigDecimal.ZERO));
		}
	}

	
}
