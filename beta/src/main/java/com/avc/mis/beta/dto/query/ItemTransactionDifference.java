/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.util.Optional;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.BasicValueDTO;
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
public class ItemTransactionDifference {

	Integer itemId;
	String itemName;
	BigDecimal usedAmount;
	MeasureUnit usedMU;
	BigDecimal producedAmount;
	MeasureUnit producedMU;
	
	public ItemTransactionDifference(@NonNull Integer itemId, String itemName, 
			BigDecimal usedAmount, MeasureUnit usedMU, 
			BigDecimal producedAmount, MeasureUnit producedMU) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.producedAmount = producedAmount;
		this.usedMU = usedMU;
		this.usedAmount = usedAmount;
		this.producedMU = producedMU;
	}
	
	public AmountWithUnit getDifference() {
		
		AmountWithUnit produced = new AmountWithUnit(Optional.ofNullable(producedAmount).orElse(BigDecimal.ZERO), producedMU);
		AmountWithUnit used = new AmountWithUnit(Optional.ofNullable(usedAmount).orElse(BigDecimal.ZERO), usedMU);

		return produced.subtract(used);
	}
}
