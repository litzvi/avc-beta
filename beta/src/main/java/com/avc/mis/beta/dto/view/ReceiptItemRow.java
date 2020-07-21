/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
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
public class ReceiptItemRow extends ValueDTO {
	
	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	AmountWithUnit orderAmount;
//	MeasureUnit orderMU;
	OffsetDateTime receiptDate;
	AmountWithUnit receiptAmount;
//	MeasureUnit receiptMU;
	String storage;
	AmountWithUnit extraAdded;
//	MeasureUnit extraAddedMU;
	
	public ReceiptItemRow(@NonNull Integer id, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String itemName, BigDecimal orderAmount, MeasureUnit orderMU, OffsetDateTime receiptDate, 
			BigDecimal receiptAmount, MeasureUnit receiptMU, String storage, 
			BigDecimal extraAdded, MeasureUnit extraAddedMU) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode, contractTypeSuffix);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.orderAmount = new AmountWithUnit(orderAmount, orderMU);
//		this.orderMU = orderMU;
		this.receiptDate = receiptDate;
		this.receiptAmount = new AmountWithUnit(receiptAmount, receiptMU);
//		this.receiptMU = receiptMU;
		this.storage = storage;
		this.extraAdded = new AmountWithUnit(extraAdded, extraAddedMU);
//		this.extraAddedMU = extraAddedMU;
	}
	
}
