/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.RankedAuditedDTO;
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
public class StorageDTO extends StorageBaseDTO {
	
		
	public StorageDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal, unitAmount, numberUnits, warehouseLocationId, warehouseLocationValue, remarks, clazz);
	}
	
	public StorageDTO(Storage storage) {
		super(storage);
	}
	
	public StorageDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal, unitAmount, numberUnits, warehouseLocation, remarks, clazz);
	}
	
	
	@Override
	public Storage fillEntity(Object entity) {
		Storage storage;
		if(entity instanceof Storage) {
			storage = (Storage) entity;
		}
		else {
			throw new IllegalArgumentException("Param has to be Storage class");
		}
		super.fillEntity(storage);
		
		return storage;
	}

	
	
}
