/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageMoveDTO extends UsedItemBaseDTO implements StorageBaseDTO {

	private BigDecimal unitAmount;
	private BigDecimal numberUnits;	
//	private BigDecimal accessWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;

	private String className; //to differentiate between storage to ExtraAdded nad perhaps storageMoves
	
	
	
	public StorageMoveDTO(Integer id, Integer version, Integer ordinal, BigDecimal numberUsedUnits, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit measureUnit, OffsetDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers,
			Integer storageId, Integer stoageVersion, Integer storageOrdinal, 
			BigDecimal storageUnitAmount, BigDecimal storageNumberUnits, BigDecimal storgeOtherUsedUnits, //BigDecimal storageContainerWeight,
			Integer storageWarehouseLocationId, String storageWarehouseLocationValue, String storageRemarks,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId, String warehouseLocationValue, Class<? extends Storage> clazz) {
		super(id, version, ordinal, numberUsedUnits, 
				itemId, itemValue, defaultMeasureUnit, itemUnitAmount, itemMeasureUnit, itemClazz, 
				measureUnit, itemProcessDate,
				poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName, 
				itemPoCodes, itemSuppliers,
				storageId, stoageVersion, storageOrdinal, 
				storageUnitAmount, storageNumberUnits, storgeOtherUsedUnits, //storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks);
		this.unitAmount = unitAmount.setScale(MeasureUnit.SCALE);
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
//		this.accessWeight = accessWeight;
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;

		if(clazz != null)
			this.className = clazz.getSimpleName();

	}
	
	public StorageMoveDTO(StorageMove storage) {
		super(storage);
		this.unitAmount = Optional.ofNullable(storage.getUnitAmount()).map(i -> i.setScale(MeasureUnit.SCALE)).orElse(null);
		this.numberUnits = Optional.ofNullable(storage.getNumberUnits()).map(i -> i.setScale(MeasureUnit.SCALE)).orElse(null);
//		this.accessWeight = storage.getAccessWeight();
		if(storage.getWarehouseLocation() != null) {
			this.warehouseLocation = new BasicValueEntity<Warehouse>(
					storage.getWarehouseLocation().getId(),  storage.getWarehouseLocation().getValue());
		}
		else {
			this.warehouseLocation = null;
		}
		this.className = storage.getClass().getSimpleName();
	}
	
	@JsonIgnore
	public AmountWithUnit getTotal() {
		if(getUnitAmount() == null || getNumberUnits() == null) {
			return null;
		}
		else {
			return new AmountWithUnit(getUnitAmount()
				.multiply(getNumberUnits())
//				.subtract(Optional.ofNullable(getAccessWeight()).orElse(BigDecimal.ZERO))
				, getMeasureUnit());
		}
	}
	
//	public BasicValueEntity<Warehouse> getOldWarehouseLocation() {
//		return getStorage() == null ? null: getStorage().getWarehouseLocation();
//	}

}
