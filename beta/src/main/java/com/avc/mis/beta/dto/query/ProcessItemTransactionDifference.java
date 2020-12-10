/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProcessItemTransactionDifference extends BasicDTO {

	AmountWithUnit usedAmount;
	AmountWithUnit producedAmount;
	
	public ProcessItemTransactionDifference(@NonNull Integer processItemId, 
			BigDecimal usedAmount, BigDecimal producedAmount, MeasureUnit measureUnit) {
		super(processItemId);
		this.producedAmount = new AmountWithUnit(Optional.ofNullable(producedAmount).orElse(BigDecimal.ZERO), measureUnit);
//		if(producedAmount != null)
//			this.producedAmount = new AmountWithUnit(producedAmount, measureUnit);
//		else
//			this.producedAmount = null;
		this.usedAmount = new AmountWithUnit(Optional.ofNullable(usedAmount).orElse(BigDecimal.ZERO), measureUnit);
//		if(usedAmount != null)
//			this.usedAmount = new AmountWithUnit(usedAmount, measureUnit);
//		else
//			this.usedAmount = null;
	}
	
	public AmountWithUnit getDifference() {
		return producedAmount.subtract(usedAmount);
	}
}
