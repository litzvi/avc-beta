/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ItemWeight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Deprecated
public class ItemWeightDTO extends RankedAuditedDTO {

	private BigDecimal unitAmount;
	private BigDecimal numberUnits;	
	private BigInteger numberOfSamples;
	private BigDecimal avgTestedWeight;


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
	
	public BigDecimal getUnitAmount() {
		if(this.unitAmount != null) {
			return this.unitAmount.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		}
		return null;
	}
	
	public BigDecimal getNumberUnits() {
		if(this.numberUnits != null) {
			return this.numberUnits.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		}
		return null;
	}
	
	public BigDecimal getAvgTestedWeight() {
		BigDecimal toReturn = Optional.ofNullable(this.avgTestedWeight).orElse(this.unitAmount);
		if(toReturn != null) {
			return toReturn.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		}
		return null;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ItemWeight.class;
	}
	
	@Override
	public ItemWeight fillEntity(Object entity) {
		ItemWeight itemWeight;
		if(entity instanceof ItemWeight) {
			itemWeight = (ItemWeight) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ItemWeight class");
		}
		super.fillEntity(itemWeight);

		itemWeight.setUnitAmount(getUnitAmount());
		itemWeight.setNumberUnits(getNumberUnits());
		itemWeight.setNumberOfSamples(getNumberOfSamples());
		itemWeight.setAvgTestedWeight(getAvgTestedWeight());
		
		return itemWeight;
	}
	
	
}
