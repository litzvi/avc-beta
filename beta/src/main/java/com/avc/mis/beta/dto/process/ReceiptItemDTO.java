/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.dto.values.ObjectWithIdAndVersion;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.process.ReceiptItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptItemDTO extends ProcessItemDTO {
	
	private ObjectWithIdAndVersion orderItemId;

	public ReceiptItemDTO(Integer id, Integer version, Item item, PoCode itemPo, 
			String description, String remarks, Integer orderItemId, Integer orderItemVersion) {
		super(id, version, item, itemPo, description, remarks);
		this.orderItemId = new ObjectWithIdAndVersion(orderItemId, orderItemVersion);
	}

	
	public ReceiptItemDTO(ReceiptItem receiptItem) {
		super(receiptItem);
		if(receiptItem.getOrderItem() != null)
			this.orderItemId = new ObjectWithIdAndVersion(receiptItem.getOrderItem());
	}

}
