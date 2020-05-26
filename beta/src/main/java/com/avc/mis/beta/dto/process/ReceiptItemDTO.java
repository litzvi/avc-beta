/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Set;

import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.dto.values.DataObject;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.dto.values.ReceiptItemWithStorage;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.ReceiptItem;
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

	public ReceiptItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName,
			String description, String remarks, Integer orderItemId, Integer orderItemVersion) {
		super(id, version, itemId, itemValue, poCodeId, contractTypeCode, supplierName, description, remarks);
		this.orderItem = new DataObject(orderItemId, orderItemVersion);
	}

	
	public ReceiptItemDTO(ReceiptItem receiptItem) {
		super(receiptItem);
		if(receiptItem.getOrderItem() != null)
			this.orderItem = new DataObject(receiptItem.getOrderItem());
	}


	public ReceiptItemDTO(Integer id, Integer version, ValueObject item, PoCodeDTO itemPo, 
			String description, String remarks, DataObject orderItem) {
		super(id, version, item, itemPo, description, remarks);
		this.orderItem = orderItem;
	}
	
	
}
