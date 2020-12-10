/**
 * 
 */
package com.avc.mis.beta.dto.query;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.avc.mis.beta.dto.view.ProcessItemInventory;
import com.avc.mis.beta.dto.view.StorageInventoryRow;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.utilities.CollectionItemWithGroup;

import lombok.Value;

/**
 * DTO used to fetch inventory information from database with a select constructor query.
 * Contains process item inventory information and storage details that includes the used amounts.
 * 
 * @author Zvi
 *
 */
@Value
public class InventoryProcessItemWithStorage implements CollectionItemWithGroup<StorageInventoryRow, ProcessItemInventory>{

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
			Integer itemId, String itemValue, MeasureUnit defaultMeasureUnit, 
			ItemGroup group, ProductionUse productionUse, Class<? extends Item> clazz,
			MeasureUnit measureUnit, 
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime processDate, OffsetDateTime receiptDate, boolean tableView,
			Integer storageId, Integer storageVersion, Integer storageOrdinal,
			BigDecimal unitAmount, BigDecimal numberUnits, BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			BigDecimal numberUsedUnits, 
			BigDecimal totalBalance, MeasureUnit totalBalanceMU) {

		this.processItemInventoryRow = new ProcessItemInventory(
				processItemId, itemId, itemValue, defaultMeasureUnit, group, productionUse, clazz, measureUnit,
				poCodeId, contractTypeCode, contractTypeSuffix, supplierName,
				processDate, receiptDate, tableView);
		this.storageInventoryRow = new StorageInventoryRow(
				storageId, storageVersion, storageOrdinal,
				processItemId,
				unitAmount, numberUnits, accessWeight,
				warehouseLocationId, warehouseLocationValue,
				numberUsedUnits, 
				totalBalance, totalBalanceMU); 
	}

//	@Override
//	public Integer getGroupId() {
//		return getProcessItemInventoryRow().getId();
//	}

	@Override
	public StorageInventoryRow getItem() {
		return getStorageInventoryRow();
	}

	@Override
	public ProcessItemInventory getGroup() {
		return getProcessItemInventoryRow();
	}

}
