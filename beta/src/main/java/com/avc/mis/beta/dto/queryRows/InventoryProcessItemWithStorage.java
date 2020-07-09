/**
 * 
 */
package com.avc.mis.beta.dto.queryRows;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.entities.enums.ContractTypeCode;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Value;

/**
 * @author Zvi
 *
 */
@Value
public class InventoryProcessItemWithStorage {

	ProcessItemInventoryRow processItemInventoryRow;
	//for ProcessItemInventoryRow
//	Integer processItemId;
//	BasicValueEntity<Item> item;
//	PoCodeDTO poCode;
//	OffsetDateTime receiptDate;
	
	StorageInventoryRow storageInventoryRow;
	//for StorageInventoryRow
//	DataObject<Storage> storage;
//	AmountWithUnit unitAmount;
//	BigDecimal numberUnits;	
//	BasicValueEntity<Warehouse> warehouseLocation;
//	BigDecimal usedUnits;
//	AmountWithUnit totalBalance;
	
	public InventoryProcessItemWithStorage(
			Integer processItemId, 
			Integer itemId, String itemValue,
			Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName,
			OffsetDateTime receiptDate,
			Integer storageId, Integer storageVersion, 
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal usedUnits, 
			BigDecimal totalBalance, MeasureUnit totalBalanceMU) {

		this.processItemInventoryRow = new ProcessItemInventoryRow(
				processItemId, itemId, itemValue,
				poCodeId, contractTypeCode, supplierName,
				receiptDate);
//		this.processItemId = processItemId;
//		this.item = new BasicValueEntity<Item>(itemId, itemValue);
//		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, supplierName);
//		this.receiptDate = receiptDate;
		
		this.storageInventoryRow = new StorageInventoryRow(
				storageId, storageVersion, processItemId,
				unitAmount, measureUnit, numberUnits, 
				warehouseLocationId, warehouseLocationValue,
				usedUnits, 
				totalBalance, totalBalanceMU); 
//				this.unitAmount, this.numberUnits, this.warehouseLocation, 
//				this.usedUnits, this.totalBalance);
//		this.storage = new DataObject<Storage>(storageId, storageVersion);
//		this.processItemId = processItemId;
//		this.unitAmount = new AmountWithUnit(unitAmount, measureUnit);;
//		this.numberUnits = numberUnits;
//		if(warehouseLocationId != null && warehouseLocationValue != null)
//			this.warehouseLocation = new BasicValueEntity<Warehouse>(warehouseLocationId,  warehouseLocationValue);
//		else
//			this.warehouseLocation = null;
//		this.usedUnits = usedUnits;
//		this.totalBalance = new AmountWithUnit(totalBalance, totalBalanceMU);
		
	}
	
//	public ProcessItemInventoryRow getProcessItemInventoryRow() {
//		return new ProcessItemInventoryRow(this.processItemId, item, poCode, receiptDate);
//	}
//	
//	public StorageInventoryRow getStorageInventoryRow() {
//		return new StorageInventoryRow(storage.getId(), storage.getVersion(), this.processItemId, 
//				this.unitAmount, this.numberUnits, this.warehouseLocation, 
//				this.usedUnits, this.totalBalance);
//	}
}
