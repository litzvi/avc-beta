/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.avc.mis.beta.dto.process.group.StorageMovesGroupDTO;
import com.avc.mis.beta.dto.process.storages.StorageMoveDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.NonNull;

/**
 * For fetching with query storage moves with their group.
 * 
 * @author zvi
 *
 */
@Data
public class StorageMoveWithGroup {

	StorageMovesGroupDTO storageMovesGroup;
	StorageMoveDTO storageMove;
	
	public StorageMoveWithGroup(@NonNull Integer id, Integer version,  Integer ordinal, 
			String groupName, boolean tableView,
			Integer moveId, Integer moveVersion, Integer moveOrdinal, BigDecimal numberUsedUnits, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit movedMeasureUnit, LocalDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers, 
			Integer gradeId,  String gradeValue,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal, 
			BigDecimal storageUnitAmount, BigDecimal storageNumberUnits, BigDecimal storgeOtherUsedUnits, 
			Integer storageWarehouseLocationId, String storageWarehouseLocationValue, String storageRemarks,
			BigDecimal unitAmount, BigDecimal numberUnits, 
			Integer warehouseLocationId, String warehouseLocationValue, Class<? extends Storage> clazz) {
		this.storageMovesGroup = new StorageMovesGroupDTO(id, version, ordinal, groupName, tableView);
		this.storageMove = new StorageMoveDTO(moveId, moveVersion, moveOrdinal, numberUsedUnits, 
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				movedMeasureUnit, itemProcessDate,
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, 
				itemPoCodes, itemSuppliers, 
				gradeId, gradeValue,
				storageId, stoageVersion, storageOrdinal, 
				storageUnitAmount, storageNumberUnits, storgeOtherUsedUnits, //storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks,
				unitAmount, numberUnits, //accessWeight,
				warehouseLocationId, warehouseLocationValue, clazz);
	}
}
