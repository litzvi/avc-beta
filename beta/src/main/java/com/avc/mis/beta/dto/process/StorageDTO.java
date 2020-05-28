/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.Storage;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class StorageDTO extends ProcessDTO {

	BigDecimal unitAmount;
	MeasureUnit measureUnit;
	BigDecimal numberUnits;	
	BasicValueEntity<Warehouse> warehouseLocation;
	String remarks;
	
	/**
	 * 
	 */
	public StorageDTO(Integer id, Integer version,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks) {
		super(id, version);
		this.unitAmount = unitAmount.setScale(3);
		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(3);
		this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		this.remarks = remarks;
	}
	
	/**
	 * @param id
	 * @param version
	 */
	public StorageDTO(Storage storage) {
		super(storage.getId(), storage.getVersion());
		this.measureUnit = storage.getMeasureUnit();
		this.unitAmount = storage.getUnitAmount().setScale(3);
		this.numberUnits = storage.getNumberUnits().setScale(3);
		this.warehouseLocation = new BasicValueEntity<Warehouse>(
				storage.getWarehouseLocation().getId(),  storage.getWarehouseLocation().getValue());
		this.remarks = storage.getRemarks();
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
	public StorageDTO(Integer id, Integer version,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			BasicValueEntity<Warehouse> warehouseLocation, String remarks) {
		super(id, version);
		this.unitAmount = unitAmount.setScale(3);
		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(3);
		this.warehouseLocation = warehouseLocation;
		this.remarks = remarks;
	}
	
	
}
