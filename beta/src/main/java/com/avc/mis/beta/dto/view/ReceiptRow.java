/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.OffsetDateTime;
import java.util.List;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;

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

	AmountWithUnit[] totalAmount;
	OffsetDateTime receiptDate;
	List<ReceiptItemRow> receiptRows;

	public ReceiptRow(@NonNull Integer id, AmountWithUnit totalAmount, List<ReceiptItemRow> receiptRows) {
		super(id);
		this.totalAmount = new AmountWithUnit[2];
		this.totalAmount[0] = totalAmount.setScale(MeasureUnit.SCALE);
		this.totalAmount[1] = totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
		this.receiptRows = receiptRows;
		if(receiptRows != null && !receiptRows.isEmpty()) {
			this.receiptDate = receiptRows.get(0).getReceiptDate();
		}
		else {
			this.receiptDate = null;
		}
	}
	
}
