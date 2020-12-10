/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.process.inventory.StorageMoveDTO;
import com.avc.mis.beta.dto.processinfo.StorageMovesGroupDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class StorageMoveWithGroup implements CollectionItemWithGroup<StorageMoveDTO, StorageMovesGroupDTO> {

	StorageMovesGroupDTO storageMovesGroup;
	StorageMoveDTO storageMove;
	
	public StorageMoveWithGroup(@NonNull Integer id, Integer version,  Integer ordinal, 
			MeasureUnit measureUnit, String groupName, boolean tableView,
			Integer moveId, Integer moveVersion, Integer moveOrdinal, BigDecimal numberUsedUnits, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit movedMeasureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal, 
			BigDecimal storageUnitAmount, BigDecimal storageNumberUnits, BigDecimal storgeOtherUsedUnits, BigDecimal storageContainerWeight,
			Integer storageWarehouseLocationId, String storageWarehouseLocationValue, String storageRemarks,
			BigDecimal unitAmount, BigDecimal numberUnits, BigDecimal accessWeight,
			Integer warehouseLocationId, String warehouseLocationValue, Class<? extends Storage> clazz) {
//		super(id);
		this.storageMovesGroup = new StorageMovesGroupDTO(id, version, ordinal, measureUnit, groupName, tableView);
		this.storageMove = new StorageMoveDTO(moveId, moveVersion, moveOrdinal, numberUsedUnits, 
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				movedMeasureUnit, itemProcessDate,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal, 
				storageUnitAmount, storageNumberUnits, storgeOtherUsedUnits, storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks,
				unitAmount, numberUnits, accessWeight,
				warehouseLocationId, warehouseLocationValue, clazz);
	}
	
//	public StorageMoveWithGroup(@NonNull Integer id, StorageMovesGroupDTO storageMovesGroup, StorageMoveDTO storageMove) {
//		super(id);
//		this.storageMovesGroup = storageMovesGroup;
//		this.storageMove = storageMove;
//	}

	@JsonIgnore
	@Override
	public StorageMoveDTO getItem() {
		return getStorageMove();
	}

	@JsonIgnore
	@Override
	public StorageMovesGroupDTO getGroup() {
		return getStorageMovesGroup();
	}
}
