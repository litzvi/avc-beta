/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Lob;

import org.apache.commons.lang3.tuple.Pair;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.OrdinalObject;
import com.avc.mis.beta.dto.values.ValueObject;
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
public class StorageWithSampleDTO extends StorageBaseDTO {

	private BigDecimal sampleContainerWeight;	
    private List<OrdinalObject<BigDecimal>> sampleWeights;
	private BigInteger numberOfSamples;	
	private BigDecimal avgTestedWeight;

		
	public StorageWithSampleDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId, String warehouseLocationValue, 
			String remarks, Class<? extends Storage> clazz, 
			BigDecimal sampleContainerWeight, List<OrdinalObject<BigDecimal>> sampleWeights, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, measureUnit, numberUnits, containerWeight, 
				warehouseLocationId, warehouseLocationValue, remarks,
				clazz);
		this.sampleContainerWeight = sampleContainerWeight;
		this.sampleWeights = sampleWeights;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
	
	public StorageWithSampleDTO(StorageWithSample storage) {
		super(storage);
		if(storage.getSampleContainerWeight() != null)
			this.sampleContainerWeight = storage.getSampleContainerWeight().setScale(MeasureUnit.SCALE);
		
		this.sampleWeights = storage.getSampleWeights();

		if(storage.getNumberOfSamples() != null)
			this.numberOfSamples = storage.getNumberOfSamples();
		if(storage.getAvgTestedWeight() != null)
			this.avgTestedWeight = storage.getAvgTestedWeight().setScale(MeasureUnit.SCALE);
	}
	
	public StorageWithSampleDTO(Integer id, Integer version, Integer ordinal,
			AmountWithUnit unitAmount, BigDecimal numberUnits, BigDecimal containerWeight,
			BasicValueEntity<Warehouse> warehouseLocation, String remarks, Class<? extends Storage> clazz, 
			BigDecimal sampleContainerWeight, List<OrdinalObject<BigDecimal>> sampleWeights, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal, unitAmount, numberUnits, containerWeight, warehouseLocation, remarks, clazz);
		this.sampleContainerWeight = sampleContainerWeight;
		this.sampleWeights = sampleWeights;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight;
	}
	
	public BigDecimal getAvgTestedWeight() {
		if(sampleWeights != null) {
			 return sampleWeights.stream().map(OrdinalObject<BigDecimal>::getValue)
					.reduce(BigDecimal::add)
					.map(s -> s.divide(new BigDecimal(sampleWeights.size()), MathContext.DECIMAL64))
					.orElseGet(() -> avgTestedWeight);
		}
		return avgTestedWeight;
	}
	
	public BigInteger getNumberOfSamples() {
		if(sampleWeights != null) {
			return BigInteger.valueOf(sampleWeights.size());
		}
		return numberOfSamples;
	}
		
	public AmountWithUnit getWeighedDifferance() {		
		
		BigDecimal acumelatedAvg = getAvgTestedWeight();
		if(acumelatedAvg == null) {
			return null;
		}
		if(sampleContainerWeight != null) {
			acumelatedAvg = avgTestedWeight.subtract(sampleContainerWeight);
		}
		return new AmountWithUnit(acumelatedAvg
				.subtract(getUnitAmount().getAmount())
				.multiply(getNumberUnits()), getUnitAmount().getMeasureUnit());
	}
	
	
	/**
	 * Gets a new StorageWithSample with all user set fields in the DTO (excluding id, version) 
	 * with given numerUnits and new warehouse location.
	 * @param numberUnits new storage number of units
	 * @param newLocation the new warehouse location
	 * @return StorageWithSample with all fields besides for the ones managed by the persistence context. 
	 */
	@Override
	public Storage getNewStorage(BigDecimal numberUnits, Warehouse newLocation) {
		StorageWithSample storage = new StorageWithSample();
		setNewStorageFields(storage, numberUnits, newLocation);
		return storage;
	}
	
	/**
	 * Receives a StorageWithSample and fills in all user set fields of this StorageWithSampleDTO
	 * @param storage StorageWithSample to set with this dto's data
	 * @param numberUnits new storage number of units
	 * @param newLocation the new warehouse location
	 */
	protected void setNewStorageFields(StorageWithSample storage, BigDecimal numberUnits, Warehouse newLocation) {
		super.setNewStorageFields(storage, numberUnits, newLocation);
		storage.setSampleContainerWeight(this.sampleContainerWeight);
		storage.setNumberOfSamples(this.numberOfSamples);
		storage.setAvgTestedWeight(this.avgTestedWeight);		
	}

}
