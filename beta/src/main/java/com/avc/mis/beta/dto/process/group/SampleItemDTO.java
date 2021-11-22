/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.process.collectionItems.ItemWeightDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collectionItems.ItemWeight;
import com.avc.mis.beta.entities.process.group.SampleItem;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Deprecated
public class SampleItemDTO extends RankedAuditedDTO {
	
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
	
	@Override
	public SampleItem fillEntity(Object entity) {
		SampleItem sampleItem;
		if(entity instanceof SampleItem) {
			sampleItem = (SampleItem) entity;
		}
		else {
			throw new IllegalStateException("Param has to be SampleItem class");
		}
		super.fillEntity(sampleItem);

		try {
			sampleItem.setItem((Item) getItem().fillEntity(new Item()));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Item is mandatory");
		}
		sampleItem.setMeasureUnit(getMeasureUnit());
		sampleItem.setSampleContainerWeight(getSampleContainerWeight());
		if(getItemWeights() == null || getItemWeights().isEmpty()) {
			throw new IllegalArgumentException("Sample item requires at least one item weight");
		}
		else {
			Ordinal.setOrdinals(getItemWeights());
			sampleItem.setItemWeights(getItemWeights().stream().map(i -> i.fillEntity(new ItemWeight())).collect(Collectors.toSet()));
		}
		
		return sampleItem;
	}
	
}
