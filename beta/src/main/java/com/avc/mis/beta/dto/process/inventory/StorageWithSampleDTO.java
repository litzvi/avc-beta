/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import com.avc.mis.beta.dto.basic.OrdinalAmount;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageWithSample;
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
//		this.sampleContainerWeight = sampleContainerWeight;
		this.sampleWeights = sampleWeights;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
	
	public StorageWithSampleDTO(StorageWithSample storage) {
		super(storage);
//		if(storage.getSampleContainerWeight() != null)
//			this.sampleContainerWeight = storage.getSampleContainerWeight().setScale(MeasureUnit.SCALE);
		
		this.sampleContainerWeights = storage.getSampleContainerWeights();
		this.sampleWeights = storage.getSampleWeights();

		if(storage.getNumberOfSamples() != null)
			this.numberOfSamples = storage.getNumberOfSamples();
		if(storage.getAvgTestedWeight() != null)
			this.avgTestedWeight = storage.getAvgTestedWeight().setScale(MeasureUnit.SCALE);
	}
	
	public StorageWithSampleDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, BigDecimal accessWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz, 
			List<OrdinalAmount<BigDecimal>> sampleContainerWeights, 
			List<OrdinalAmount<BigDecimal>> sampleWeights, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, //accessWeight, 
				warehouseLocation, remarks, clazz);
		this.sampleContainerWeights = sampleContainerWeights;
//		this.sampleContainerWeight = sampleContainerWeight;
		this.sampleWeights = sampleWeights;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
	
	
	public BigDecimal getSampleContainerWeight() {
//		Optional<BigDecimal> sampleContainerWeight = Optional.ofNullable(this.sampleContainerWeight);
//		if(sampleContainerWeights != null) {
//			sampleContainerWeight = sampleContainerWeights.stream().map(OrdinalAmount<BigDecimal>::getAmount)
//					.reduce(BigDecimal::add)
//					.map(s -> s.divide(new BigDecimal(sampleContainerWeights.size()), MathContext.DECIMAL64));
//		}
		
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
	
	//done in UsedItemBaseDTO
//	
//	/**
//	 * Gets a new StorageWithSample with all user set fields in the DTO (excluding id, version) 
//	 * with given numerUnits and new warehouse location.
//	 * @param numberUnits new storage number of units
//	 * @param newLocation the new warehouse location
//	 * @return StorageWithSample with all fields besides for the ones managed by the persistence context. 
//	 */
//	@Override
//	public Storage getNewStorage(BigDecimal numberUnits, Warehouse newLocation) {
//		StorageWithSample storage = new StorageWithSample();
//		setNewStorageFields(storage, numberUnits, newLocation);
//		return storage;
//	}
//	
//	/**
//	 * Receives a StorageWithSample and fills in all user set fields of this StorageWithSampleDTO
//	 * @param storage StorageWithSample to set with this dto's data
//	 * @param numberUnits new storage number of units
//	 * @param newLocation the new warehouse location
//	 */
//	protected void setNewStorageFields(StorageWithSample storage, BigDecimal numberUnits, Warehouse newLocation) {
//		super.setNewStorageFields(storage, numberUnits, newLocation);
//		storage.setSampleContainerWeight(this.sampleContainerWeight);
//		storage.setNumberOfSamples(this.numberOfSamples);
//		storage.setAvgTestedWeight(this.avgTestedWeight);		
//	}

}
