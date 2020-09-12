/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ItemWeight;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ItemWeightDTO extends ProcessDTO {

	BigDecimal unitAmount;
	BigDecimal numberUnits;	
	BigInteger numberOfSamples;
	BigDecimal avgTestedWeight;


	public ItemWeightDTO(Integer id, Integer version,
			BigDecimal unitAmount, BigDecimal numberUnits, 
			BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version);
		this.unitAmount = unitAmount.setScale(MeasureUnit.SCALE);
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.numberOfSamples = numberOfSamples;
		if(avgTestedWeight != null)
			this.avgTestedWeight = avgTestedWeight.setScale(MeasureUnit.SCALE);
		else
			this.avgTestedWeight = null;
	}


	public ItemWeightDTO(ItemWeight itemWeight) {
		super(itemWeight.getId(), itemWeight.getVersion());
		this.unitAmount = itemWeight.getUnitAmount().setScale(MeasureUnit.SCALE);
		this.numberUnits = itemWeight.getNumberUnits().setScale(MeasureUnit.SCALE);
		this.numberOfSamples = itemWeight.getNumberOfSamples();
		if(itemWeight.getAvgTestedWeight() != null)
			this.avgTestedWeight = itemWeight.getAvgTestedWeight().setScale(MeasureUnit.SCALE);
		else
			this.avgTestedWeight = null;
		
	}
	
	public BigDecimal getAvgTestedWeight() {
		return Optional.ofNullable(this.avgTestedWeight).orElse(this.unitAmount);
	}
	
	
}
