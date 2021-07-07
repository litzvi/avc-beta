/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProcessStatus;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ReceiptRow extends BasicDTO {

	LocalDateTime receiptDate;
	ProcessStatus status;
	List<ReceiptItemRow> receiptRows;

	public ReceiptRow(@NonNull Integer id, List<ReceiptItemRow> receiptRows) {
		super(id);
		this.receiptRows = receiptRows;
		if(receiptRows != null && !receiptRows.isEmpty()) {
			ReceiptItemRow itemRow = receiptRows.get(0);
			this.receiptDate = itemRow.getReceiptDate();
			this.status = itemRow.getStatus();
		}
		else {
			this.receiptDate = null;
			this.status = null;
		}
		
	}

	//perhaps total weight
	public List<AmountWithUnit> getTotalAmount() {
		if(receiptRows == null || receiptRows.isEmpty()) {
			return null;
		}
		AmountWithUnit totalAmount;
		try {
			totalAmount = receiptRows.stream()
					.map(pi -> pi.getReceiptAmt())
					.reduce(AmountWithUnit::add).get();
		} catch (UnsupportedOperationException e) {
			return null;
		}
		
		if(!MeasureUnit.WEIGHT_UNITS.contains(totalAmount.getMeasureUnit())) {
			return Arrays.asList(totalAmount);
		}
		else {
			return AmountWithUnit.weightDisplay(totalAmount, Arrays.asList(totalAmount.getMeasureUnit(), MeasureUnit.LOT));
		}
	}

}
