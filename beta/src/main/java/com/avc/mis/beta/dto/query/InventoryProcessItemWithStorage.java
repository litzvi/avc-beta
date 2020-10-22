/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.view.ProcessItemInventory;
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

	ProcessItemInventory processItemInventoryRow;
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
			OffsetDateTime processDate, OffsetDateTime receiptDate, boolean tableView,
			Integer storageId, Integer storageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, BigDecimal containerWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal numberUsedUnits, 
			BigDecimal totalBalance, MeasureUnit totalBalanceMU) {

		this.processItemInventoryRow = new ProcessItemInventory(
				processItemId, itemId, itemValue, itemCategory,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				processDate, receiptDate, tableView);
		this.storageInventoryRow = new StorageInventoryRow(
				storageId, storageVersion, storageOrdinal,
				processItemId,
				unitAmount, measureUnit, numberUnits, containerWeight,
				warehouseLocationId, warehouseLocationValue,
				numberUsedUnits, 
				totalBalance, totalBalanceMU); 
	}

}
