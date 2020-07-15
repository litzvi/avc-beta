/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StorageDTO extends ProcessDTO {

	private AmountWithUnit unitAmount;
//	MeasureUnit measureUnit;
	private BigDecimal numberUnits;	
	private BasicValueEntity<Warehouse> warehouseLocation;
	private String remarks;
	private String className;
//	Class<? extends Storage> clazz;
	
	public StorageDTO(Integer id, Integer version,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks, Class<? extends Storage> clazz) {
		super(id, version);
		this.unitAmount = new AmountWithUnit(unitAmount.setScale(MeasureUnit.SCALE), measureUnit);
//		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		if(warehouseLocationId != null && warehouseLocationValue != null)
			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
		else
			this.warehouseLocation = null;
		this.remarks = remarks;
		this.className = clazz.getSimpleName();
	}
	
	/**
	 * @param id
	 * @param version
	 */
	public StorageDTO(Storage storage) {
		super(storage.getId(), storage.getVersion());
//		this.measureUnit = storage.getMeasureUnit();
		this.unitAmount = storage.getUnitAmount().setScale(MeasureUnit.SCALE);
		this.numberUnits = storage.getNumberUnits().setScale(MeasureUnit.SCALE);
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
	public StorageDTO(Integer id, Integer version,
			AmountWithUnit unitAmount, BigDecimal numberUnits, 
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz) {
		super(id, version);
		this.unitAmount = unitAmount;
//		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.warehouseLocation = warehouseLocation;
		this.remarks = remarks;
		this.className = clazz.getSimpleName();
	}
	
	
}
