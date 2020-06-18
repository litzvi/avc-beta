/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Item;

import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class ItemWithProcessAndStorageInventoryRow {

	BasicValueEntity<Item> item;
//	AmountWithUnit itemStock;
	
	PoCodeDTO poCode;
	OffsetDateTime receiptDate;
	AmountWithUnit poDateAmount;
	
//	StorageInventoryRow storage;
	
	public ItemWithProcessAndStorageInventoryRow(Integer itemId, String itemValue,
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName,
			OffsetDateTime receiptDate, BigDecimal poDateAmount, MeasureUnit itemMU,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue) {
		super();
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, supplierName);
		this.receiptDate = receiptDate;
		this.poDateAmount = new AmountWithUnit(
				poDateAmount.setScale(AmountWithUnit.SCALE, RoundingMode.HALF_DOWN), itemMU);
//		this.storage = new StorageInventoryRow(unitAmount, measureUnit, numberUnits, 
//				warehouseLocationId,  warehouseLocationValue);
	}
	
	
	
}
