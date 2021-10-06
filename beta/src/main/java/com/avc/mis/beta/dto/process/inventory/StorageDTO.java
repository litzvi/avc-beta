/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageDTO extends SubjectDataDTO implements StorageBaseDTO {
	
	private BigDecimal unitAmount;
	private BigDecimal numberUnits;	
//	private BigDecimal accessWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;
//	private BigDecimal numberUsedUnits;

	private String className; //to differentiate between storage to ExtraAdded nad perhaps storageMoves
		
	public StorageDTO(Integer id, Integer version, Integer ordinal,
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
	public StorageDTO(StorageBase storage) {
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

	
	public StorageDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal);
		this.unitAmount = unitAmount;
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
//		this.accessWeight = accessWeight.setScale(MeasureUnit.SCALE);
		this.warehouseLocation = warehouseLocation;
		this.className = clazz.getSimpleName();
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
	
	
	
	
}
