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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ItemTransactionDifference extends ValueDTO {

	String itemName;
	AmountWithUnit amountUsed;
	AmountWithUnit producedAmount;
	
	public ItemTransactionDifference(@NonNull Integer itemId, String itemName, 
			BigDecimal usedAmount, MeasureUnit usedMU, 
			BigDecimal producedAmount, MeasureUnit producedMU) {
		super(itemId);
		this.itemName = itemName;
		this.amountUsed = new AmountWithUnit(usedAmount, usedMU);
		this.producedAmount = new AmountWithUnit(producedAmount, producedMU);
	}
	
	public AmountWithUnit getDifference() {
		return Optional.ofNullable(producedAmount).orElse(AmountWithUnit.ZERO)
				.substract(Optional.ofNullable(amountUsed).orElse(AmountWithUnit.ZERO));
	}
}
