/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ExtraAddedDTO extends StorageWithSampleDTO {
	
	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, BigDecimal unitAmount,
			BigDecimal numberUnits, BigDecimal accessWeight, BasicValueEntity<Warehouse> warehouseLocation,
			String remarks, Class<? extends Storage> clazz, BigInteger numberOfSamples,
			BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, accessWeight, warehouseLocation, remarks, clazz,
				null, null, numberOfSamples, avgTestedWeight);
	}

	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, BigDecimal unitAmount, MeasureUnit measureUnit,
			BigDecimal numberUnits, //BigDecimal accessWeight, 
			Integer warehouseLocationId,
			String warehouseLocationValue, String remarks, Class<? extends Storage> clazz,
			BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, //accessWeight, 
				warehouseLocationId,
				warehouseLocationValue, remarks, clazz, null, null, numberOfSamples, avgTestedWeight);
	}

	public ExtraAddedDTO(ExtraAdded extraAdded) {
		super(extraAdded);
	}
	
//	/**
//	 * Gets a new ExtraAdded with all user set fields in the DTO (excluding id, version) 
//	 * with given numerUnits and new warehouse location.
//	 * @param numberUnits new storage number of units
//	 * @param newLocation the new warehouse location
//	 * @return ExtraAdded with all fields besides for the ones managed by the persistence context. 
//	 */
//	@Override
//	public Storage getNewStorage(BigDecimal numberUnits, Warehouse newLocation) {
//		ExtraAdded storage = new ExtraAdded();
//		setNewStorageFields(storage, numberUnits, newLocation);
//		return storage;
//	}
	
	@Override
	public ExtraAdded fillEntity(Object entity) {
		ExtraAdded extraAdded;
		if(entity instanceof ExtraAdded) {
			extraAdded = (ExtraAdded) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ExtraAdded class");
		}
		super.fillEntity(extraAdded);
		
		return extraAdded;
	}

	
}
