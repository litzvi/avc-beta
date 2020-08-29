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
import com.avc.mis.beta.entities.processinfo.StorageWithSample;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExtraAddedDTO extends StorageWithSampleDTO {
	
	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, AmountWithUnit unitAmount,
			BigDecimal numberUnits, BigDecimal containerWeight, BasicValueEntity<Warehouse> warehouseLocation,
			String remarks, Class<? extends Storage> clazz, BigDecimal sampleContainerWeight, BigInteger numberOfSamples,
			BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, containerWeight, warehouseLocation, remarks, clazz,
				sampleContainerWeight, numberOfSamples, avgTestedWeight);
	}

	public ExtraAddedDTO(Integer id, Integer version, Integer ordinal, BigDecimal unitAmount, MeasureUnit measureUnit,
			BigDecimal numberUnits, BigDecimal containerWeight, Integer warehouseLocationId,
			String warehouseLocationValue, String remarks, Class<? extends Storage> clazz,
			BigDecimal sampleContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, measureUnit, numberUnits, containerWeight, warehouseLocationId,
				warehouseLocationValue, remarks, clazz, sampleContainerWeight, numberOfSamples, avgTestedWeight);
	}

	public ExtraAddedDTO(ExtraAdded extraAdded) {
		super(extraAdded);
	}

	
}
