/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.storages.ExtraAdded;
import com.avc.mis.beta.entities.process.storages.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Used for Items received separately from the the order as compensation.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ExtraAddedDTO extends StorageWithSampleDTO {
	
	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, BigDecimal unitAmount, MeasureUnit measureUnit,
			BigDecimal numberUnits, //BigDecimal accessWeight, 
			Integer warehouseLocationId,
			String warehouseLocationValue, String remarks, Class<? extends Storage> clazz,
			BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, //accessWeight, 
				warehouseLocationId,
				warehouseLocationValue, remarks, clazz, null, null, numberOfSamples, avgTestedWeight);
	}
	
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
