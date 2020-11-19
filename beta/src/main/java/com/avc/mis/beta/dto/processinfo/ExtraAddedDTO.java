/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ExtraAdded;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExtraAddedDTO extends StorageWithSampleDTO {
	
	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, BigDecimal unitAmount,
			BigDecimal numberUnits, BigDecimal containerWeight, BasicValueEntity<Warehouse> warehouseLocation,
			String remarks, Class<? extends Storage> clazz, BigDecimal sampleContainerWeight, BigInteger numberOfSamples,
			BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, containerWeight, warehouseLocation, remarks, clazz,
				null, sampleContainerWeight, null, numberOfSamples, avgTestedWeight);
	}

	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, BigDecimal unitAmount, MeasureUnit measureUnit,
			BigDecimal numberUnits, BigDecimal containerWeight, Integer warehouseLocationId,
			String warehouseLocationValue, String remarks, Class<? extends Storage> clazz,
			BigDecimal sampleContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, containerWeight, warehouseLocationId,
				warehouseLocationValue, remarks, clazz, null, sampleContainerWeight, null, numberOfSamples, avgTestedWeight);
	}

	public ExtraAddedDTO(ExtraAdded extraAdded) {
		super(extraAdded);
	}
	
	/**
	 * Gets a new ExtraAdded with all user set fields in the DTO (excluding id, version) 
	 * with given numerUnits and new warehouse location.
	 * @param numberUnits new storage number of units
	 * @param newLocation the new warehouse location
	 * @return ExtraAdded with all fields besides for the ones managed by the persistence context. 
	 */
	@Override
	public Storage getNewStorage(BigDecimal numberUnits, Warehouse newLocation) {
		ExtraAdded storage = new ExtraAdded();
		setNewStorageFields(storage, numberUnits, newLocation);
		return storage;
	}

	
}
