/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.OffsetDateTime;
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

//	AmountWithUnit[] totalAmount;
	OffsetDateTime receiptDate;
	ProcessStatus status;
	List<ReceiptItemRow> receiptRows;

	public ReceiptRow(@NonNull Integer id, List<ReceiptItemRow> receiptRows) {
		super(id);
//		this.totalAmount = new AmountWithUnit[2];
//		this.totalAmount[0] = totalAmount.setScale(MeasureUnit.SCALE);
//		this.totalAmount[1] = totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
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
	public AmountWithUnit[] getTotalAmount() {
		AmountWithUnit totalAmount = receiptRows.stream()
				.map(pi -> pi.getReceiptAmt())
				.filter(u -> MeasureUnit.WEIGHT_UNITS.contains(u.getMeasureUnit()))
				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);

		return new AmountWithUnit[] {
				totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};

	}

}
