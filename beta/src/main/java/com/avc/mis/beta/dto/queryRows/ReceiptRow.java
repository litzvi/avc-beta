/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptRow extends ValueDTO {

	AmountWithUnit totalAmount;

	List<ReceiptItemRow> receiptRows;

	public ReceiptRow(@NonNull Integer id, AmountWithUnit totalAmount, List<ReceiptItemRow> receiptRows) {
		super(id);
		this.totalAmount = totalAmount;
		this.receiptRows = receiptRows;
	}
	
}
