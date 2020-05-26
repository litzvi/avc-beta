/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.values.ValueObject;
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
	
	private ValueObject item;
	private BigDecimal unitAmount;
	private MeasureUnit measureUnit;
	private BigInteger numberOfSamples;	
	private BigDecimal avgTestedWeight;
	private BigDecimal emptyContainerWeight;


	public SampleItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			BigDecimal unitAmount, MeasureUnit measureUnit, BigInteger numberOfSamples, 
			BigDecimal avgTestedWeight, BigDecimal emptyContainerWeight) {
		super(id, version);
		this.item = new ValueObject(itemId, itemValue);
		this.unitAmount = unitAmount.setScale(3);
		this.measureUnit = measureUnit;
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight.setScale(3);
		this.emptyContainerWeight = emptyContainerWeight.setScale(3);
	}

	public SampleItemDTO(@NonNull SampleItem sampleItem) {
		super(sampleItem.getId(), sampleItem.getVersion());
		this.item = new ValueObject(sampleItem.getItem());
		this.unitAmount = sampleItem.getUnitAmount().setScale(3);
		this.measureUnit = sampleItem.getMeasureUnit();
		this.numberOfSamples = sampleItem.getNumberOfSamples();
		this.avgTestedWeight = sampleItem.getAvgTestedWeight().setScale(3);
		this.emptyContainerWeight = sampleItem.getEmptyContainerWeight().setScale(3);


	}
	
	
}
