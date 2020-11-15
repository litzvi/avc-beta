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
import com.avc.mis.beta.entities.processinfo.Storage;

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
	
	public StorageMoveWithGroup(@NonNull Integer id, Integer version,  Integer ordinal, String groupName, boolean tableView,
			
			Integer moveId, Integer moveVersion, Integer moveOrdinal, BigDecimal numberUsedUnits, 
			Integer itemId, String itemValue, OffsetDateTime itemProcessDate,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal, BigDecimal storageUnitAmount,
			MeasureUnit storageMeasureUnit, BigDecimal storageNumberUnits, BigDecimal storageContainerWeight,
			Integer storageWarehouseLocationId, String storageWarehouseLocationValue, String storageRemarks,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId, String warehouseLocationValue, Class<? extends Storage> clazz) {
		super(id);
		this.storageMovesGroup = new StorageMovesGroupDTO(id, version, ordinal, groupName, tableView);
		this.storageMove = new StorageMoveDTO(moveId, moveVersion, moveOrdinal, numberUsedUnits, 
				itemId, itemValue, itemProcessDate,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal, storageUnitAmount,
				storageMeasureUnit, storageNumberUnits, storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks,
				unitAmount, measureUnit, numberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, clazz);
		}
	
	
	public StorageMoveWithGroup(@NonNull Integer id, StorageMovesGroupDTO storageMovesGroup, StorageMoveDTO storageMove) {
		super(id);
		this.storageMovesGroup = storageMovesGroup;
		this.storageMove = storageMove;
	}
}
