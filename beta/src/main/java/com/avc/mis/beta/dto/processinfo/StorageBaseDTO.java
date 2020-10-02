/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.StorageBase;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageBaseDTO extends SubjectDataDTO {

//	private String name;
	private AmountWithUnit unitAmount;
	private BigDecimal numberUnits;	
	private BigDecimal containerWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;
	private String remarks;
	private String className; //to differentiate between storage to ExtraAdded
//	Class<? extends Storage> clazz;
		
	public StorageBaseDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal);
//		this.name = name;
		this.unitAmount = new AmountWithUnit(unitAmount.setScale(MeasureUnit.SCALE), measureUnit);
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.containerWeight = containerWeight;
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
		this.remarks = remarks;
		if(clazz != null)
			this.className = clazz.getSimpleName();
	}
	
	/**
	 * @param id
	 * @param version
	 */
	public StorageBaseDTO(StorageBase storage) {
		super(storage.getId(), storage.getVersion(), storage.getOrdinal());
//		this.name = storage.getName();
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
		this.remarks = storage.getRemarks();
		this.className = storage.getClass().getSimpleName();
	}

	/**
	 * @param storageId
	 * @param storageVersion
	 * @param unitAmount2
	 * @param measureUnit2
	 * @param numberUnits2
	 * @param warehouseLocation2
	 * @param description
	 */
	public StorageBaseDTO(Integer id, Integer version, Integer ordinal,
			AmountWithUnit unitAmount, BigDecimal numberUnits, BigDecimal containerWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal);
//		this.name = name;
		this.unitAmount = unitAmount;
//		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.containerWeight = containerWeight.setScale(MeasureUnit.SCALE);
		this.warehouseLocation = warehouseLocation;
		this.remarks = remarks;
		this.className = clazz.getSimpleName();
	}
	
	/**
	 * Gets a new Storage with all user set fields in the DTO (excluding id, version) 
	 * with given numerUnits and new warehouse location.
	 * @param numberUnits new storage number of units
	 * @param newLocation the new warehouse location
	 * @return Storage with all fields besides for the ones managed by the persistence context. 
	 */
	public Storage getNewStorage(BigDecimal numberUnits, Warehouse newLocation) {
		Storage storage = new Storage();
		setNewStorageFields(storage, numberUnits, newLocation);
		return storage;
	}
	
	/**
	 * Receives a Storage and fills in all user set fields of this StorageDto
	 * @param storage Storage to set with this dto's data
	 * @param numberUnits new storage number of units
	 * @param newLocation the new warehouse location
	 */
	protected void setNewStorageFields(Storage storage, BigDecimal numberUnits, Warehouse newLocation) {
		storage.setOrdinal(this.getOrdinal());
		storage.setUnitAmount(this.unitAmount);
		storage.setContainerWeight(this.containerWeight);
		storage.setNumberUnits(numberUnits);
		storage.setWarehouseLocation(newLocation);
	}

	
	
	
}
