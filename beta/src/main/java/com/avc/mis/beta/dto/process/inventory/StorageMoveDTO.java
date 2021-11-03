/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StorageMoveDTO extends UsedItemBaseDTO implements StorageBaseInterface {

	private BigDecimal unitAmount;
	private BigDecimal numberUnits;	
//	private BigDecimal accessWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;

	@JsonIgnore //for referencing storage move to processItem besides for it's group before persist/merge
	@EqualsAndHashCode.Exclude
	private DataObject<ProcessItem> processItem;
	@EqualsAndHashCode.Exclude
	private String className; //to differentiate between storage to ExtraAdded nad perhaps storageMoves
	
	
	
	public StorageMoveDTO(Integer id, Integer version, Integer ordinal, BigDecimal numberUsedUnits, 
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			BigDecimal itemUnitAmount, MeasureUnit itemMeasureUnit, Class<? extends Item> itemClazz, 
			MeasureUnit measureUnit, LocalDateTime itemProcessDate,
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemPoCodes, String itemSuppliers, 
			Integer gradeId,  String gradeValue,
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
				gradeId, gradeValue,
				storageId, stoageVersion, storageOrdinal, 
				storageUnitAmount, storageNumberUnits, storgeOtherUsedUnits, //storageContainerWeight,
				storageWarehouseLocationId, storageWarehouseLocationValue, storageRemarks);
		if(unitAmount != null)
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
	public BigDecimal getTotal() {
		if(getNumberUnits() == null) {
			return null;
		}
		else if(getUnitAmount() == null) {
			return getNumberUnits();
		}
		else {
			return getUnitAmount()
				.multiply(getNumberUnits());
		}
	}
	
//	public BasicValueEntity<Warehouse> getOldWarehouseLocation() {
//		return getStorage() == null ? null: getStorage().getWarehouseLocation();
//	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return StorageMove.class;
	}
	
	@Override
	public StorageMove fillEntity(Object entity) {
		StorageMove storageMove;
		if(entity instanceof StorageMove) {
			storageMove = (StorageMove) entity;
		}
		else {
			throw new IllegalStateException("Param has to be StorageMove class");
		}
		super.fillEntity(storageMove);
		if(getStorage() == null) {
			throw new IllegalArgumentException("Internal error: Used item has no referance to storage");
		}
		storageMove.setUnitAmount(getUnitAmount());
//		storageMove.setNumberUnits(getNumberUnits());
		if(getWarehouseLocation() != null)
			storageMove.setWarehouseLocation((Warehouse) getWarehouseLocation().fillEntity(new Warehouse()));
		try {
			storageMove.setProcessItem((ProcessItem) getProcessItem().fillEntity(new ProcessItem()));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Internal error: Used item has no referance to process item");
		}
		
		return storageMove;
	}

}
