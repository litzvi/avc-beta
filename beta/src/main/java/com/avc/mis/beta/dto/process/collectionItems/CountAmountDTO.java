/**
 * 
 */
package com.avc.mis.beta.dto.process.collectionItems;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collectionItems.CountAmount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO class with the amount(weight). Part of a collection in ItemCountDTO class.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CountAmountDTO extends RankedAuditedDTO {

	private BigDecimal amount;
	
	public CountAmountDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version, ordinal);
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		if(this.amount != null)
			return this.amount.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN);
		return null;
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return CountAmount.class;
	}
	
	@Override
	public CountAmount fillEntity(Object entity) {
		CountAmount countAmount;
		if(entity instanceof CountAmount) {
			countAmount = (CountAmount) entity;
		}
		else {
			throw new IllegalStateException("Param has to be CountAmount class");
		}
		super.fillEntity(countAmount);
		countAmount.setAmount(getAmount());
		
		return countAmount;
	}

}
