/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.StorageMove;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageMoveDTO extends UsedItemBaseDTO {

	private Integer ordinal;
	private AmountWithUnit unitAmount;
	private BigDecimal numberUnits;	
	private BigDecimal containerWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;

	private String className; //to differentiate between storage to ExtraAdded nad perhaps storageMoves
	
	public StorageMoveDTO(Integer id, Integer version, BigDecimal numberUsedUnits, Integer itemId, String itemValue,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal, BigDecimal storageUnitAmount,
			MeasureUnit storageMeasureUnit, BigDecimal storageNumberUnits, BigDecimal storageContainerWeight,
			Integer storageWarehouseLocationId, String storageWarehouseLocationValue, String storageRemarks,
			Integer ordinal, BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId, String warehouseLocationValue, Class<? extends Storage> clazz) {
		super(id, version, numberUsedUnits, itemId, itemValue, poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				storageId, stoageVersion, storageOrdinal, storageUnitAmount, storageMeasureUnit, storageNumberUnits, storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks);
		this.ordinal = ordinal;
		this.unitAmount = new AmountWithUnit(unitAmount.setScale(MeasureUnit.SCALE), measureUnit);
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.containerWeight = containerWeight;
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;

		if(clazz != null)
			this.className = clazz.getSimpleName();

	}
	
	public StorageMoveDTO(StorageMove storage) {
		super(storage);
		this.ordinal = storage.getOrdinal();
		this.unitAmount = Optional.ofNullable(storage.getUnitAmount()).map(i -> i.setScale(MeasureUnit.SCALE)).orElse(null);
		this.numberUnits = Optional.ofNullable(storage.getNumberUnits()).map(i -> i.setScale(MeasureUnit.SCALE)).orElse(null);
		this.containerWeight = storage.getContainerWeight();
		if(storage.getWarehouseLocation() != null) {
			this.warehouseLocation = new BasicValueEntity<Warehouse>(
					storage.getWarehouseLocation().getId(),  storage.getWarehouseLocation().getValue());
		}
		else {
			this.warehouseLocation = null;
		}
		this.className = storage.getClass().getSimpleName();
	}

}
