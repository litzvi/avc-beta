/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.SampleItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SampleItemDTO extends ProcessDTO {
	
	private BasicValueEntity item;
	private BigDecimal unitAmount;
	private MeasureUnit measureUnit;
//	private BigInteger numberOfSamples;	
//	private BigDecimal avgTestedWeight;
	private BigDecimal emptyContainerWeight;
	
	private MultiSet<ItemWeightDTO> itemWeights;


	public SampleItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal emptyContainerWeight) {
		super(id, version);
		this.item = new BasicValueEntity(itemId, itemValue);
		this.unitAmount = unitAmount.setScale(3);
		this.measureUnit = measureUnit;
//		this.numberOfSamples = numberOfSamples;
//		this.avgTestedWeight = avgTestedWeight.setScale(3);
		this.emptyContainerWeight = emptyContainerWeight.setScale(3);
	}

	public SampleItemDTO(@NonNull SampleItem sampleItem) {
		super(sampleItem.getId(), sampleItem.getVersion());
		this.item = new BasicValueEntity(sampleItem.getItem());
		this.unitAmount = sampleItem.getUnitAmount().setScale(3);
		this.measureUnit = sampleItem.getMeasureUnit();
//		this.numberOfSamples = sampleItem.getNumberOfSamples();
//		this.avgTestedWeight = sampleItem.getAvgTestedWeight().setScale(3);
		this.emptyContainerWeight = sampleItem.getEmptyContainerWeight().setScale(3);
		
		this.itemWeights = Arrays.stream(sampleItem.getItemWeights())
				.map(i->{return new ItemWeightDTO(i);})
				.collect(Collectors.toCollection(() -> {return new HashMultiSet<ItemWeightDTO>();}));

	}
	
	public void setItemWeights(Collection<ItemWeightDTO> itemWeights) {
		this.itemWeights = new HashMultiSet<ItemWeightDTO>(itemWeights);
	}
	
	
}
