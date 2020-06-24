/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.processinfo.ItemWeight;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ItemWeightDTO extends ProcessDTO {

	BigInteger numberOfSamples;
	BigDecimal avgTestedWeight;


	public ItemWeightDTO(Integer id, Integer version, BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version);
		this.numberOfSamples = numberOfSamples;
		this.avgTestedWeight = avgTestedWeight.setScale(AmountWithUnit.SCALE);
	}


	public ItemWeightDTO(ItemWeight itemWeight) {
		super(itemWeight.getId(), itemWeight.getVersion());
		this.numberOfSamples = itemWeight.getNumberOfSamples();
		this.avgTestedWeight = itemWeight.getAvgTestedWeight().setScale(AmountWithUnit.SCALE);
	}
	
	
}
