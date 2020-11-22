/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.processinfo.StorageMoveDTO;
import com.avc.mis.beta.dto.processinfo.StorageMovesGroupDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class StorageMoveWithGroup extends ValueDTO {

	StorageMovesGroupDTO storageMovesGroup;
	StorageMoveDTO storageMove;
	
	public StorageMoveWithGroup(@NonNull Integer id, Integer version,  Integer ordinal, 
			MeasureUnit measureUnit, String groupName, boolean tableView,
			Integer moveId, Integer moveVersion, Integer moveOrdinal, BigDecimal numberUsedUnits, 
			Integer itemId, String itemValue, MeasureUnit movedMeasureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal, BigDecimal storageUnitAmount,
			BigDecimal storageNumberUnits, BigDecimal storageContainerWeight,
			Integer storageWarehouseLocationId, String storageWarehouseLocationValue, String storageRemarks,
			BigDecimal unitAmount, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId, String warehouseLocationValue, Class<? extends Storage> clazz) {
		super(id);
		this.storageMovesGroup = new StorageMovesGroupDTO(id, version, ordinal, measureUnit, groupName, tableView);
		this.storageMove = new StorageMoveDTO(moveId, moveVersion, moveOrdinal, numberUsedUnits, 
				itemId, itemValue, movedMeasureUnit, itemProcessDate,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal, storageUnitAmount,
				storageNumberUnits, storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks,
				unitAmount, numberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, clazz);
		}
	
	
	public StorageMoveWithGroup(@NonNull Integer id, StorageMovesGroupDTO storageMovesGroup, StorageMoveDTO storageMove) {
		super(id);
		this.storageMovesGroup = storageMovesGroup;
		this.storageMove = storageMove;
	}
}
