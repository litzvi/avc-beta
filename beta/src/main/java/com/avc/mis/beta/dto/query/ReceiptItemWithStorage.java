/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.avc.mis.beta.dto.processinfo.ReceiptItemDTO;
import com.avc.mis.beta.dto.processinfo.StorageWithSampleDTO;
import com.avc.mis.beta.dto.values.DataObject;
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
	private StorageWithSampleDTO storage;
	
	private DataObject<OrderItem> orderItem;
	
	
	public ReceiptItemWithStorage(Integer id, Integer version, Integer itemId, String itemValue, 
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			Integer storageId, Integer storageVersion,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks, 
			Class<? extends Storage> clazz,
			BigDecimal emptyContainerWeight, BigInteger numberOfSamples, BigDecimal avgTestedWeight,
			String description, String remarks, 
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit extraMU) {
		this.receiptItem = new ReceiptItemDTO(id, version, itemId, itemValue, 
				/* poCodeId, contractTypeCode, supplierName, */
				description, remarks, orderItemId, orderItemVersion, extraRequested, extraMU);
		this.storage = new StorageWithSampleDTO(storageId, storageVersion, 
				unitAmount, measureUnit, numberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue, storageRemarks, clazz,
				emptyContainerWeight, numberOfSamples, avgTestedWeight);
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
