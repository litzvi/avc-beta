/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.DTOWithId;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

/**
 * Used for getting inventory balance of units for Storage entity object.
 * 
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class StorageBalance extends DTOWithId {

	BigDecimal numberUnits;
	BigDecimal usedAmount;
	
	public StorageBalance(@NonNull Integer id, @NonNull BigDecimal numberUnits, BigDecimal usedAmount) {
		super(id);
		this.numberUnits = numberUnits;
		this.usedAmount = usedAmount;
	}
	
	public BigDecimal getBalance() {
		if(usedAmount == null) {
			return numberUnits;
		}
		return numberUnits.subtract(usedAmount);
	}
	
	public Boolean isLegal() {
		if(usedAmount == null)
			return true;
		return this.usedAmount.compareTo(this.numberUnits) <= 0;
	}
	
	

}
