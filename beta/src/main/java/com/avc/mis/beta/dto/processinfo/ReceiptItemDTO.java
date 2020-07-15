/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.DataObject;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ReceiptItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptItemDTO extends ProcessItemDTO {
	
	private DataObject orderItem;
	private AmountWithUnit extraRequested;
//	private MeasureUnit measureUnit;

//	private Set<StorageDTO> extraAdded; //can use a SortedSet like ContactDetails to maintain order	
	
	public ReceiptItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			String description, String remarks, 
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit measureUnit) {
		super(id, version, itemId, itemValue, /* poCodeId, contractTypeCode, supplierName, */description, remarks);
		if(orderItemId != null)
			this.orderItem = new DataObject(orderItemId, orderItemVersion);
		if(extraRequested != null) {
			this.extraRequested = new AmountWithUnit(extraRequested.setScale(MeasureUnit.SCALE), measureUnit);
		}
	}

	
	public ReceiptItemDTO(ReceiptItem receiptItem) {
		super(receiptItem);
		if(receiptItem.getOrderItem() != null)
			this.orderItem = new DataObject(receiptItem.getOrderItem());
		if(receiptItem.getExtraRequested() != null) {
			this.extraRequested = receiptItem.getExtraRequested().setScale(MeasureUnit.SCALE);
		}
//		this.measureUnit = receiptItem.getMeasureUnit();
	}


	public ReceiptItemDTO(Integer id, Integer version,
			BasicValueEntity<Item> item, /* PoCodeDTO itemPo, */ 
			String description, String remarks, 
			DataObject orderItem, AmountWithUnit extraRequested) {
		super(id, version, item, /* itemPo, */ description, remarks);
		this.orderItem = orderItem;
		if(extraRequested != null) {
			this.extraRequested = extraRequested.setScale(MeasureUnit.SCALE);
		}
//		this.measureUnit = measureUnit;
	}
	
	
}
