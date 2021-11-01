/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.ItemWeight;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class ItemWeightDTO extends SubjectDataDTO {

	BigDecimal unitAmount;
	BigDecimal numberUnits;	
	BigInteger numberOfSamples;
	BigDecimal avgTestedWeight;


	public ItemWeightDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, 
			BigInteger numberOfSamples, BigDecimal avgTestedWeight) {
		super(id, version, ordinal);
		this.unitAmount = unitAmount.setScale(MeasureUnit.SCALE);
		this.numberUnits = numberUnits.setScale(MeasureUnit.SCALE);
		this.numberOfSamples = numberOfSamples;
		if(avgTestedWeight != null)
			this.avgTestedWeight = avgTestedWeight.setScale(MeasureUnit.SCALE);
		else
			this.avgTestedWeight = null;
	}


	public ItemWeightDTO(ItemWeight itemWeight) {
		super(itemWeight.getId(), itemWeight.getVersion(), itemWeight.getOrdinal());
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
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ItemWeight.class;
	}
	
	
}
