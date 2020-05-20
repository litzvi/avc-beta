/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PoCode;

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
	
	String value;
	String supplierName;
	String itemName;
	BigDecimal orderAmount;
	MeasureUnit orderMU;
	OffsetDateTime receiptDate;
	BigDecimal receiptAmount;
	MeasureUnit receiptMU;
//	BigDecimal addedAmount;
	String storage;
	
	
	public ReceiptRow(@NonNull Integer id, PoCode poCode, String supplierName, String itemName, BigDecimal orderAmount,
			MeasureUnit orderMU, OffsetDateTime receiptDate, BigDecimal receiptAmount, MeasureUnit receiptMU, 
			String storage) {
		super(id);
		this.value = poCode.getValue();
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.orderAmount = orderAmount;
		this.orderMU = orderMU;
		this.receiptDate = receiptDate;
		this.receiptAmount = receiptAmount;
		this.receiptMU = receiptMU;
//		this.addedAmount = addedAmount;
		this.storage = storage;
	}
	
}
