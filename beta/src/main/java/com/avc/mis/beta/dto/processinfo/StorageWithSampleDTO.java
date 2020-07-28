/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.Column;
import javax.validation.constraints.Positive;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
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
public class StorageWithSampleDTO extends StorageDTO {

	private BigDecimal emptyContainerWeight;	
	private BigInteger numberOfSamples;	
	private BigDecimal avgTestedWeight;
	
	
	public StorageWithSampleDTO(Integer id, Integer version, 
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId, String warehouseLocationValue, 
			String remarks, Class<? extends Storage> clazz, 
			BigDecimal emptyContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, unitAmount, measureUnit, numberUnits, warehouseLocationId, warehouseLocationValue, remarks,
				clazz);
		this.emptyContainerWeight = emptyContainerWeight;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
	
	public StorageWithSampleDTO(StorageWithSample storage) {
		super(storage);
		if(storage.getEmptyContainerWeight() != null)
			this.emptyContainerWeight = storage.getEmptyContainerWeight().setScale(MeasureUnit.SCALE);
		if(storage.getNumberOfSamples() != null)
			this.numberOfSamples = storage.getNumberOfSamples();
		if(storage.getAvgTestedWeight() != null)
			this.avgTestedWeight = storage.getAvgTestedWeight().setScale(MeasureUnit.SCALE);
	}
	
	public StorageWithSampleDTO(Integer id, Integer version,
			AmountWithUnit unitAmount, BigDecimal numberUnits, 
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz, 
			BigDecimal emptyContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, unitAmount, numberUnits, warehouseLocation, remarks, clazz);
		this.emptyContainerWeight = emptyContainerWeight;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}

}
