/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StorageBaseDTO extends RankedAuditedDTO implements StorageBaseInterface {
	
	private BigDecimal unitAmount;
	private BigDecimal numberUnits;	
//	private BigDecimal accessWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;
//	private BigDecimal numberUsedUnits;

	@EqualsAndHashCode.Exclude
	private String className; //to differentiate between storage to ExtraAdded nad perhaps storageMoves
		
	public StorageBaseDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal);
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
	
	/**
	 * @param id
	 * @param version
	 */
	public StorageBaseDTO(StorageBase storage) {
		super(storage.getId(), storage.getVersion(), storage.getOrdinal());
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

	
	public StorageBaseDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal);
		this.unitAmount = unitAmount;
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
//		this.accessWeight = accessWeight.setScale(MeasureUnit.SCALE);
		this.warehouseLocation = warehouseLocation;
		this.className = clazz.getSimpleName();
	}
	
	public BigDecimal getUnitAmount() {
		if(this.unitAmount == null) {
			return null;
		}
		return this.unitAmount.setScale(MeasureUnit.SCALE);
	}
	
	public BigDecimal getNumberUnits() {
		if(this.numberUnits == null) {
			return null;
		}
		return this.numberUnits.setScale(MeasureUnit.SCALE);
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
//				.subtract(Optional.ofNullable(getAccessWeight()).orElse(BigDecimal.ZERO));
		}
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return StorageBase.class;
	}
	
	@Override
	public StorageBase fillEntity(Object entity) {
		StorageBase storageBase;
		if(entity instanceof StorageBase) {
			storageBase = (StorageBase) entity;
		}
		else {
			throw new IllegalStateException("Param has to be StorageBase class");
		}
		super.fillEntity(storageBase);
		storageBase.setUnitAmount(getUnitAmount());
		storageBase.setNumberUnits(getNumberUnits());
		if(getWarehouseLocation() != null)
			storageBase.setWarehouseLocation((Warehouse) getWarehouseLocation().fillEntity(new Warehouse()));
		
		return storageBase;
	}

	
	
}
