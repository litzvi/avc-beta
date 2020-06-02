/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
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
	
	PoCodeBasic poCode;
	String supplierName;
	String itemName;
	BigDecimal orderAmount;
	MeasureUnit orderMU;
	OffsetDateTime receiptDate;
	BigDecimal receiptAmount;
	MeasureUnit receiptMU;
	String storage;
	BigDecimal extraAdded;
//	MeasureUnit extraAddedMU;
	
	
	public ReceiptRow(@NonNull Integer id, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, 
			String itemName, BigDecimal orderAmount, MeasureUnit orderMU, OffsetDateTime receiptDate, 
			BigDecimal receiptAmount, MeasureUnit receiptMU, String storage, 
			BigDecimal extraAdded) {
		super(id);
		this.poCode = new PoCodeBasic(poCodeId, contractTypeCode);
		this.supplierName = supplierName;
		this.itemName = itemName;
		this.orderAmount = orderAmount;
		this.orderMU = orderMU;
		this.receiptDate = receiptDate;
		this.receiptAmount = receiptAmount;
		this.receiptMU = receiptMU;
		this.storage = storage;
		this.extraAdded = extraAdded;
//		this.extraAddedMU = extraAddedMU;
	}
	
}
