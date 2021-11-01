/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.RankedAuditedDTO;
import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.CountAmount;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO class with the amount(weight). Part of a collection in ItemCountDTO class.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CountAmountDTO extends RankedAuditedDTO {

	private BigDecimal amount;
	
	public CountAmountDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version, ordinal);
		this.amount = amount;
	}

	public CountAmountDTO(CountAmount countAmount) {
		super(countAmount.getId(), countAmount.getVersion(), countAmount.getOrdinal());
		this.amount = countAmount.getAmount().setScale(MeasureUnit.SCALE);
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
