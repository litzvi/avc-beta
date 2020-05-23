/**
 * 
 */
package com.avc.mis.beta.dto.process;

import java.util.Set;

import com.avc.mis.beta.dto.values.ValueObject;
import com.avc.mis.beta.dto.values.ObjectWithIdAndVersion;
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
	
	private ObjectWithIdAndVersion orderItem;

	public ReceiptItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName,
			String description, String remarks, Integer orderItemId, Integer orderItemVersion) {
		super(id, version, itemId, itemValue, poCodeId, contractTypeCode, supplierName, description, remarks);
		this.orderItem = new ObjectWithIdAndVersion(orderItemId, orderItemVersion);
	}

	
	public ReceiptItemDTO(ReceiptItem receiptItem) {
		super(receiptItem);
		if(receiptItem.getOrderItem() != null)
			this.orderItem = new ObjectWithIdAndVersion(receiptItem.getOrderItem());
	}


	public ReceiptItemDTO(Integer id, Integer version, ValueObject item, PoCodeDTO itemPo, 
			String description, String remarks, ObjectWithIdAndVersion orderItem) {
		super(id, version, item, itemPo, description, remarks);
		this.orderItem = orderItem;
	}
	
	public ReceiptItemDTO(ReceiptItemWithStorage ri) {
		super(ri.getId(), ri.getVersion(), ri.getItem(), ri.getItemPo(), 
				ri.getDescription(), ri.getRemarks());
		this.orderItem = ri.getOrderItem();
	}

}
