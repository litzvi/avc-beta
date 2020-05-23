/**
 * 
 */
package com.avc.mis.beta.dto.values;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.process.ReceiptItemDTO;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.PoCode;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReceiptItemWithStorage extends ProcessItemWithStorage {

	private DataObject orderItem;

	public ReceiptItemWithStorage(Integer id, Integer version, Integer itemId, String itemValue, 
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, 
			Integer storageId, Integer storageVersion,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			String description, String remarks, Integer orderItemId, Integer orderItemVersion) {
		super(id, version, itemId, itemValue, 
				poCodeId, contractTypeCode, supplierName,
				storageId, storageVersion,
				unitAmount, measureUnit, numberUnits, warehouseLocationId,  warehouseLocationValue, 
				description, remarks);
		this.orderItem = new DataObject(orderItemId, orderItemVersion);
	}
	
	public ReceiptItemDTO getReceiptItem() {
		return new ReceiptItemDTO(getId(), getVersion(), getItem(), getItemPo(), 
				getDescription(), getRemarks(), getOrderItem());
	}
}
