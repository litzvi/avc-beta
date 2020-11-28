/**
 * 
 */
package com.avc.mis.beta.dto.query;

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
@Deprecated
public class SampleItemWithWeight {

	private SampleItemDTO sampleItem;
	private ItemWeightDTO itemWeight;
	
	public SampleItemWithWeight(Integer id, Integer version,  Integer ordinal,Integer itemId, String itemValue, 
			MeasureUnit measureUnit, BigDecimal sampleContainerWeight,
			Integer itemWeightId, Integer itemWeightVersion,  Integer itemWeightOrdinal,
			BigDecimal unitAmount, BigDecimal numberUnits, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		this.sampleItem = new SampleItemDTO(id, version, ordinal, itemId, itemValue, 
			measureUnit, sampleContainerWeight);
		this.itemWeight = new ItemWeightDTO(itemWeightId, itemWeightVersion, itemWeightOrdinal,
				unitAmount, numberUnits, numberOfSamples, avgTestedWeight);
	}
	
	/**
	 * @return id of SampleItem. 
	 * Used for mapping to the logical structure that every SampleItem has a collection of ItemWeights.
	 */
	public Integer getId() {
		return sampleItem.getId();
	}

}
