/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.process.storages.StorageBase;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Used for representing and referencing inventory storage.
 * Contains unit amount of the storage, number of units and warehouse location.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StorageBaseDTO extends RankedAuditedDTO {
	
	private BigDecimal unitAmount;
	private BigDecimal numberUnits;	
//	private BigDecimal accessWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;

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
