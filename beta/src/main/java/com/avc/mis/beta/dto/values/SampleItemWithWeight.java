/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.process.ItemWeightDTO;
import com.avc.mis.beta.dto.process.SampleItemDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class SampleItemWithWeight {

	private SampleItemDTO sampleItem;
	private ItemWeightDTO itemWeight;
	
	public SampleItemWithWeight(Integer id, Integer version, Integer itemId, String itemValue, 
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal emptyContainerWeight,
			Integer itemWeightId, Integer itemWeightVersion, 
			BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		this.sampleItem = new SampleItemDTO(id, version, itemId, itemValue, 
			unitAmount, measureUnit, emptyContainerWeight);
		this.itemWeight = new ItemWeightDTO(itemWeightId, itemWeightVersion, numberOfSamples, avgTestedWeight);
	}
	
	/**
	 * @return id of SampleItem. 
	 * Used for mapping to the logical structure that every SampleItem has a collection of ItemWeights.
	 */
	public Integer getId() {
		return sampleItem.getId();
	}

}
