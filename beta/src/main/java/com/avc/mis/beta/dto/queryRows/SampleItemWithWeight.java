/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.processinfo.ItemWeightDTO;
import com.avc.mis.beta.dto.processinfo.SampleItemDTO;
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
			MeasureUnit measureUnit, BigDecimal emptyContainerWeight,
			Integer itemWeightId, Integer itemWeightVersion, 
			BigDecimal unitAmount, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		this.sampleItem = new SampleItemDTO(id, version, itemId, itemValue, 
			measureUnit, emptyContainerWeight);
		this.itemWeight = new ItemWeightDTO(itemWeightId, itemWeightVersion, unitAmount, numberOfSamples, avgTestedWeight);
	}
	
	/**
	 * @return id of SampleItem. 
	 * Used for mapping to the logical structure that every SampleItem has a collection of ItemWeights.
	 */
	public Integer getId() {
		return sampleItem.getId();
	}

}
