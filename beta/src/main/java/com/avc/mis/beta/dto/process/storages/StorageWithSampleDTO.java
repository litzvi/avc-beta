/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.process.storages.StorageWithSample;
import com.avc.mis.beta.utilities.OrdinalAmount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Used for representing received item. 
 * Includes sampling information for this storage.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class StorageWithSampleDTO extends StorageDTO {
	
	private static final Integer SAMPLE_SCALE = 5;

	private List<OrdinalAmount<BigDecimal>> sampleContainerWeights;
//	private BigDecimal sampleContainerWeight;	
    private List<OrdinalAmount<BigDecimal>> sampleWeights;
	private BigInteger numberOfSamples;	
	private BigDecimal avgTestedWeight;

		
	public StorageWithSampleDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId, String warehouseLocationValue, 
			String remarks, Class<? extends Storage> clazz, 
			List<OrdinalAmount<BigDecimal>> sampleContainerWeights, 
			List<OrdinalAmount<BigDecimal>> sampleWeights, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, //accessWeight, 
				warehouseLocationId, warehouseLocationValue, remarks,
				clazz);
		this.sampleContainerWeights = sampleContainerWeights;
		this.sampleWeights = sampleWeights;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
	
	public BigDecimal getSampleContainerWeight() {
		if(this.sampleContainerWeights != null) {
			Optional<BigDecimal> sampleContainerWeight = this.sampleContainerWeights.stream().map(OrdinalAmount<BigDecimal>::getAmount)
					.reduce(BigDecimal::add)
					.map(s -> s.divide(new BigDecimal(sampleContainerWeights.size()), MathContext.DECIMAL64));
			return sampleContainerWeight.map(w -> w.setScale(SAMPLE_SCALE, RoundingMode.HALF_DOWN)).orElse(null);
		}
		else {
			return null;
		}

	}
	
	public BigDecimal getAvgTestedWeight() {
		Optional<BigDecimal> avgTestedWeight = Optional.ofNullable(this.avgTestedWeight);
		if(sampleWeights != null) {
			 avgTestedWeight = sampleWeights.stream().map(OrdinalAmount<BigDecimal>::getAmount)
					.reduce(BigDecimal::add)
					.map(s -> s.divide(new BigDecimal(sampleWeights.size()), MathContext.DECIMAL64).add(getUnitAmount()));
		}
		return avgTestedWeight.map(w -> w.setScale(SAMPLE_SCALE, RoundingMode.HALF_DOWN)).orElse(null);
	}
	
	public BigInteger getNumberOfSamples() {
		if(sampleWeights != null) {
			return BigInteger.valueOf(sampleWeights.size());
		}
		return numberOfSamples;
	}	
		
	public BigDecimal getWeighedDifferance() {		
		BigDecimal acumelatedAvg = getAvgTestedWeight();
		if(acumelatedAvg == null) {
			return null;
		}
		BigDecimal sampleContainerWeight = getSampleContainerWeight();
		if(sampleContainerWeight != null) {
			acumelatedAvg = acumelatedAvg.subtract(sampleContainerWeight);
		}
		return acumelatedAvg.subtract(getUnitAmount())
				.multiply(getNumberUnits())
				.setScale(SAMPLE_SCALE, RoundingMode.HALF_DOWN);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return StorageWithSample.class;
	}
	

	@Override
	public StorageWithSample fillEntity(Object entity) {
		StorageWithSample storage;
		if(entity instanceof StorageWithSample) {
			storage = (StorageWithSample) entity;
		}
		else {
			throw new IllegalStateException("Param has to be StorageWithSample class");
		}
		super.fillEntity(storage);
		storage.setSampleContainerWeights(getSampleContainerWeights());
		storage.setSampleWeights(getSampleWeights());
		storage.setNumberOfSamples(getNumberOfSamples());
		storage.setAvgTestedWeight(getAvgTestedWeight());
		
		return storage;
	}
	
}
