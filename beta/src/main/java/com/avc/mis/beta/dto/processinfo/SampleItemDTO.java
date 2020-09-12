/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.SampleItem;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SampleItemDTO extends ProcessDTO {
	
	private BasicValueEntity<Item> item;
//	private BigDecimal amountWeighed;
	private MeasureUnit measureUnit;
//	private BigInteger numberOfSamples;	
//	private BigDecimal avgTestedWeight;
	private BigDecimal sampleContainerWeight;
	
	private MultiSet<ItemWeightDTO> itemWeights;


	public SampleItemDTO(Integer id, Integer version, 
			Integer itemId, String itemValue, 
			MeasureUnit measureUnit, BigDecimal sampleContainerWeight) {
		super(id, version);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
//		this.unitAmount = amountWeighed.setScale(3);
		this.measureUnit = measureUnit;
//		this.numberOfSamples = numberOfSamples;
//		this.avgTestedWeight = avgTestedWeight.setScale(3);
		this.sampleContainerWeight = sampleContainerWeight.setScale(MeasureUnit.SCALE);
	}

	public SampleItemDTO(@NonNull SampleItem sampleItem) {
		super(sampleItem.getId(), sampleItem.getVersion());
		this.item = new BasicValueEntity<Item>(sampleItem.getItem());
		this.measureUnit = sampleItem.getMeasureUnit();
//		this.numberOfSamples = sampleItem.getNumberOfSamples();
//		this.avgTestedWeight = sampleItem.getAvgTestedWeight().setScale(3);
		this.sampleContainerWeight = sampleItem.getSampleContainerWeight().setScale(MeasureUnit.SCALE);
		
		this.itemWeights = Arrays.stream(sampleItem.getItemWeights())
				.map(i->{return new ItemWeightDTO(i);})
				.collect(Collectors.toCollection(() -> {return new HashMultiSet<ItemWeightDTO>();}));

	}
	
	public void setItemWeights(Collection<ItemWeightDTO> itemWeights) {
		this.itemWeights = new HashMultiSet<ItemWeightDTO>(itemWeights);
	}
	
	@JsonIgnore
	public Optional<AmountWithUnit> getSampleEstimate() {
		return itemWeights.stream()
			.map(iw -> Optional.ofNullable(iw.getAvgTestedWeight()).orElse(iw.getUnitAmount())
					.subtract(this.sampleContainerWeight)
					.multiply(iw.getNumberUnits()))
			.reduce(BigDecimal::add)
			.map(s -> new AmountWithUnit(s, this.measureUnit));
	}
	
	public Optional<AmountWithUnit> getRecordedSum() {
		return itemWeights.stream()
			.map(iw -> iw.getUnitAmount()
					.multiply(iw.getNumberUnits()))
			.reduce(BigDecimal::add)
			.map(s -> new AmountWithUnit(s, this.measureUnit));
	}
	
	public Optional<AmountWithUnit> getWeighedDifferance() {
		return itemWeights.stream()
				.map(iw -> Optional.ofNullable(iw.getAvgTestedWeight())
						.orElse(iw.getUnitAmount().add(this.sampleContainerWeight)) //give recorded plus bag
						.subtract(iw.getUnitAmount())
						.subtract(this.sampleContainerWeight)
						.multiply(iw.getNumberUnits()))
				.reduce(BigDecimal::add)
				.map(s -> new AmountWithUnit(s, this.measureUnit));
	}
	
}
