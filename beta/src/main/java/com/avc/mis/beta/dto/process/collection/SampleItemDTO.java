/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.SampleItem;
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
@Deprecated
public class SampleItemDTO extends SubjectDataDTO {
	
	private BasicValueEntity<Item> item;
//	private BigDecimal amountWeighed;
	private MeasureUnit measureUnit;
//	private BigInteger numberOfSamples;	
//	private BigDecimal avgTestedWeight;
	private BigDecimal sampleContainerWeight;
	
	private List<ItemWeightDTO> itemWeights;


	public SampleItemDTO(Integer id, Integer version,  Integer ordinal,
			Integer itemId, String itemValue, 
			MeasureUnit measureUnit, BigDecimal sampleContainerWeight) {
		super(id, version, ordinal);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
//		this.unitAmount = amountWeighed.setScale(3);
		this.measureUnit = measureUnit;
//		this.numberOfSamples = numberOfSamples;
//		this.avgTestedWeight = avgTestedWeight.setScale(3);
		this.sampleContainerWeight = sampleContainerWeight.setScale(MeasureUnit.SCALE);
	}

	public SampleItemDTO(@NonNull SampleItem sampleItem) {
		super(sampleItem.getId(), sampleItem.getVersion(), sampleItem.getOrdinal());
		this.item = new BasicValueEntity<Item>(sampleItem.getItem());
		this.measureUnit = sampleItem.getMeasureUnit();
//		this.numberOfSamples = sampleItem.getNumberOfSamples();
//		this.avgTestedWeight = sampleItem.getAvgTestedWeight().setScale(3);
		this.sampleContainerWeight = sampleItem.getSampleContainerWeight().setScale(MeasureUnit.SCALE);
		
		this.itemWeights = Arrays.stream(sampleItem.getItemWeights())
				.map(i->{return new ItemWeightDTO(i);})
				.collect(Collectors.toList());

	}
	
//	public void setItemWeights(List<ItemWeightDTO> itemWeights) {
//		itemWeights.sort(Ordinal.ordinalComparator());
//		this.itemWeights = itemWeights;
//	}
	
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
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return SampleItem.class;
	}
	
}
