/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.processinfo.ReceiptItemDTO;
import com.avc.mis.beta.dto.processinfo.StorageDTO;
import com.avc.mis.beta.dto.values.DataObject;
import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.OrderItem;
import com.avc.mis.beta.entities.processinfo.Storage;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class ReceiptItemWithStorage {

	private ReceiptItemDTO receiptItem;
	private StorageDTO storage;
	
	private DataObject<OrderItem> orderItem;
	
	
	public ReceiptItemWithStorage(Integer id, Integer version, Integer itemId, String itemValue, 
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			Integer storageId, Integer storageVersion,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks, 
			Class<? extends Storage> clazz,
			String description, String remarks, 
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit extraMU) {
		this.receiptItem = new ReceiptItemDTO(id, version, itemId, itemValue, 
				/* poCodeId, contractTypeCode, supplierName, */
				description, remarks, orderItemId, orderItemVersion, extraRequested, extraMU);
		this.storage = new StorageDTO(storageId, storageVersion, 
				unitAmount, measureUnit, numberUnits, 
				warehouseLocationId, warehouseLocationValue, storageRemarks, clazz);
		this.orderItem = new DataObject<OrderItem>(orderItemId, orderItemVersion);
	}
	
	/**
	 * @return id of ReceiptItem. 
	 * Used for mapping to the logical structure that every ReceiptItem has a collection of storages.
	 */
	public Integer getId() {
		return receiptItem.getId();
	}
	
}
