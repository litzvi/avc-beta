/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

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
		
	public StorageWithSampleDTO(Integer id, Integer version,  String name,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId, String warehouseLocationValue, 
			String remarks, Class<? extends Storage> clazz, 
			BigDecimal emptyContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, name, unitAmount, measureUnit, numberUnits, containerWeight, 
				warehouseLocationId, warehouseLocationValue, remarks,
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
	
	public StorageWithSampleDTO(Integer id, Integer version, String name,
			AmountWithUnit unitAmount, BigDecimal numberUnits, BigDecimal containerWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz, 
			BigDecimal emptyContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, name, unitAmount, numberUnits, containerWeight, warehouseLocation, remarks, clazz);
		this.emptyContainerWeight = emptyContainerWeight;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
		
	public AmountWithUnit getWeighedDifferance() {
		if(avgTestedWeight == null) {
			return null;
		}
		
		BigDecimal acumelatedAvg;
		if(emptyContainerWeight == null) {
			acumelatedAvg = avgTestedWeight;
		}
		else {
			acumelatedAvg = avgTestedWeight.subtract(emptyContainerWeight);
		}
		return new AmountWithUnit(acumelatedAvg
				.subtract(getUnitAmount().getAmount())
				.multiply(getNumberUnits()), getUnitAmount().getMeasureUnit());
	}

}
