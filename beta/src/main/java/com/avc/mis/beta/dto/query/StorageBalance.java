/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;

import lombok.NonNull;
import lombok.Value;

/**
 * Used for getting inventory balance of units for Storage entity object, to check if over used.
 * 
 * @author zvi
 *
 */
@Value
public class StorageBalance {

	Integer storageId;
	BigDecimal numberUnits;
	BigDecimal usedAmount;
	
	public StorageBalance(@NonNull Integer storageId, @NonNull BigDecimal numberUnits, BigDecimal usedAmount) {
		this.storageId = storageId;
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
