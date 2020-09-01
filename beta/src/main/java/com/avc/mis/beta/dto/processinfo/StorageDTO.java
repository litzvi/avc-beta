/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StorageDTO extends ProcessDTO implements Ordinal {

	private Integer ordinal;
//	private String name;
	private AmountWithUnit unitAmount;
	private BigDecimal numberUnits;	
	private BigDecimal containerWeight;	
	private BasicValueEntity<Warehouse> warehouseLocation;
	private String remarks;
	private String className; //to differentiate between storage to ExtraAdded
//	Class<? extends Storage> clazz;
	
	public StorageDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks, Class<? extends Storage> clazz) {
		super(id, version);
		this.ordinal = ordinal;
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
	public StorageDTO(Storage storage) {
		super(storage.getId(), storage.getVersion());
		this.ordinal = storage.getOrdinal();
//		this.name = storage.getName();
		this.unitAmount = storage.getUnitAmount().setScale(MeasureUnit.SCALE);
		this.numberUnits = storage.getNumberUnits().setScale(MeasureUnit.SCALE);
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
	public StorageDTO(Integer id, Integer version, Integer ordinal,
			AmountWithUnit unitAmount, BigDecimal numberUnits, BigDecimal containerWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz) {
		super(id, version);
		this.ordinal = ordinal;
//		this.name = name;
		this.unitAmount = unitAmount;
//		this.measureUnit = measureUnit;
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.containerWeight = containerWeight.setScale(MeasureUnit.SCALE);
		this.warehouseLocation = warehouseLocation;
		this.remarks = remarks;
		this.className = clazz.getSimpleName();
	}
	
	
}
