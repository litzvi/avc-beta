/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.ValueDTO;
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
public class ProcessItemTransactionDifference extends ValueDTO {

	AmountWithUnit usedAmount;
	AmountWithUnit producedAmount;
	
	public ProcessItemTransactionDifference(@NonNull Integer processItemId, 
			BigDecimal usedAmount, BigDecimal producedAmount, MeasureUnit measureUnit) {
		super(processItemId);
		if(producedAmount != null)
			this.producedAmount = new AmountWithUnit(producedAmount, measureUnit);
		else
			this.producedAmount = null;
		if(usedAmount != null)
			this.usedAmount = new AmountWithUnit(usedAmount, measureUnit);
		else
			this.usedAmount = null;
	}
	
	public AmountWithUnit getDifference() {
		return Optional.ofNullable(producedAmount).orElse(AmountWithUnit.ZERO_KG)
				.subtract(Optional.ofNullable(usedAmount).orElse(AmountWithUnit.ZERO_KG));
	}
}
