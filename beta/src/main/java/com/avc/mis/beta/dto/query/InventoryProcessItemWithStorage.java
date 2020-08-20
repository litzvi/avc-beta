/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.view.ProcessItemInventoryRow;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Value;

/**
 * DTO used to fetch inventory information from database with a select constructor query.
 * Contains process item inventory information and storage details that includes the used amounts.
 * 
 * @author Zvi
 *
 */
@Value
public class InventoryProcessItemWithStorage {

	ProcessItemInventoryRow processItemInventoryRow;
	StorageInventoryRow storageInventoryRow;
	
	/**
	 * Constructor that receives all arguments of process item and storage information
	 * including amounts used and balance, for one storage row. 
	 * All arguments are the fields the way they are stored in db, 
	 * with some aggregate result fetched directly from query.
	 */
	public InventoryProcessItemWithStorage(
			Integer processItemId, 
			Integer itemId, String itemValue, ItemCategory itemCategory,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime receiptDate,
			Integer storageId, Integer storageVersion, 
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, 
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal usedUnits, 
			BigDecimal totalBalance, MeasureUnit totalBalanceMU) {

		this.processItemInventoryRow = new ProcessItemInventoryRow(
				processItemId, itemId, itemValue, itemCategory,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				receiptDate);
		this.storageInventoryRow = new StorageInventoryRow(
				storageId, storageVersion, processItemId,
				unitAmount, measureUnit, numberUnits, 
				warehouseLocationId, warehouseLocationValue,
				usedUnits, 
				totalBalance, totalBalanceMU); 
	}

}
