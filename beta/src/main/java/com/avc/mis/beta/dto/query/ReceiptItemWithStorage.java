/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.List;

import com.avc.mis.beta.dto.data.DataObject;
import com.avc.mis.beta.dto.generic.OrdinalAmount;
import com.avc.mis.beta.dto.process.collection.ProcessItemDTO;
import com.avc.mis.beta.dto.process.collection.ReceiptItemDTO;
import com.avc.mis.beta.dto.process.inventory.ExtraAddedDTO;
import com.avc.mis.beta.dto.process.inventory.StorageBaseDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageWithSampleDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.inventory.ExtraAdded;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
//@EqualsAndHashCode(callSuper = true)
public class ReceiptItemWithStorage implements CollectionItemWithGroup<StorageDTO, ReceiptItemDTO> {

	private ReceiptItemDTO receiptItem;
	private StorageWithSampleDTO storage;
	
	private DataObject<OrderItem> orderItem;
	
	
	public ReceiptItemWithStorage(Integer id, Integer version, Integer ordinal,
			Integer itemId, String itemValue, ProductionUse productionUse, 	AmountWithUnit itemUnit, Class<? extends Item> ItemClazz,
			MeasureUnit measureUnit,
			Integer storageId, Integer storageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal numberUnits,
			Integer warehouseLocationId,  String warehouseLocationValue, String storageRemarks, 
			Class<? extends Storage> clazz,
			List<OrdinalAmount<BigDecimal>> sampleContainerWeights, 
			List<OrdinalAmount<BigDecimal>> sampleWeights, BigInteger numberOfSamples, BigDecimal avgTestedWeight, 
			String groupName, String description, String remarks, boolean tableView,
			BigDecimal receivedUnits, MeasureUnit orderMU, BigDecimal unitPrice, Currency currency,
			Integer referencedOrder,
			Integer orderItemId, Integer orderItemVersion, BigDecimal extraRequested, MeasureUnit extraMU) {
		this.receiptItem = new ReceiptItemDTO(id, version, ordinal,
				itemId, itemValue, productionUse, itemUnit, ItemClazz, measureUnit,
				groupName, description, remarks, tableView,
				receivedUnits, orderMU, unitPrice, currency,
				referencedOrder, orderItemId, orderItemVersion, extraRequested, extraMU);
		if(ExtraAdded.class.equals(clazz)) {
			this.storage = new ExtraAddedDTO(storageId, storageVersion, storageOrdinal, 
					unitAmount, measureUnit, numberUnits,
					warehouseLocationId, warehouseLocationValue, storageRemarks, clazz,
					numberOfSamples, avgTestedWeight);
		}
		else {
			this.storage = new StorageWithSampleDTO(storageId, storageVersion, storageOrdinal, 
					unitAmount, numberUnits,
					warehouseLocationId, warehouseLocationValue, storageRemarks, clazz,
					sampleContainerWeights, sampleWeights, numberOfSamples, avgTestedWeight);
		}		
		this.orderItem = new DataObject<OrderItem>(orderItemId, orderItemVersion);
	}


	@Override
	public StorageWithSampleDTO getItem() {
		return getStorage();
	}


	@Override
	public ReceiptItemDTO getGroup() {
		return getReceiptItem();
	}
	
//	/**
//	 * @return id of ReceiptItem. 
//	 * Used for mapping to the logical structure that every ReceiptItem has a collection of storages.
//	 */
//	public Integer getId() {
//		return receiptItem.getId();
//	}
	
}
