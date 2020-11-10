/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ValueDTO;
import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.processinfo.BasicStorageDTO;
import com.avc.mis.beta.dto.processinfo.StorageTableDTO;
import com.avc.mis.beta.dto.query.InventoryProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * DTO of inventory info needed for display of process item - item resulted from process.
 * Includes information about process item , process and list of StorageInventoryRow
 * that contains storage information of the processed item.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
public class ProcessItemInventory extends ValueDTO {

	private ItemDTO item;
	private PoCodeDTO poCode;
	private OffsetDateTime itemProcessDate;
	private OffsetDateTime receiptDate;
	private AmountWithUnit[] totalBalanceAmount; //not calculated in method so won't be calculated repeatedly for totalLots
	
	@JsonIgnore
	private boolean tableView;
	private List<StorageInventoryRow> storageForms;
	
	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor, 
	 * excluding list of storage forms and calculated totals.
	 */
	public ProcessItemInventory(Integer id, Integer itemId, String itemValue, ItemCategory itemCategory,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName,
			OffsetDateTime processDate, OffsetDateTime receiptDate, boolean tableView) {
		super(id);
		this.item = new ItemDTO(itemId, itemValue, null, null, itemCategory);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);
		this.itemProcessDate = processDate;
		this.receiptDate = receiptDate;
		this.tableView = tableView;
	}
	
	/**
	 * All class arguments constructor, excluding list of storage forms and calculated totals
	 */
	public ProcessItemInventory(Integer id, ItemDTO item, 
			PoCodeDTO poCode, 
			OffsetDateTime processDate, OffsetDateTime receiptDate, boolean tableView) {
		super(id);
		this.item = item;
		this.poCode = poCode;
		this.itemProcessDate = processDate;
		this.receiptDate = receiptDate;
		this.tableView = tableView;
	}
	
	
	
	/**
	 * Setter for storageForms, sets the storages for this process item 
	 * and updates total balance accordingly.
	 * @param storageForms List of StorageInventoryRow
	 */
	public void setStorageForms(List<StorageInventoryRow> storageForms) {
		if(storageForms.size() > 0) {
			this.totalBalanceAmount = new AmountWithUnit[2];
			AmountWithUnit totalBalanceAmount = storageForms.stream()
					.map(StorageInventoryRow::getTotalBalance)
					.reduce(AmountWithUnit::add).get();
			this.totalBalanceAmount[0] = totalBalanceAmount.setScale(MeasureUnit.SCALE);
			this.totalBalanceAmount[1] = totalBalanceAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
		}
		else {
			this.totalBalanceAmount = null;
		}
		this.storageForms = storageForms;
		
	}
	
	public List<StorageInventoryRow> getStorageForms() {
		if(tableView) {
			return null;
		}
		return this.storageForms;
	}
	
	public StorageTableDTO getStorage() {
		if(tableView && this.storageForms != null && !this.storageForms.isEmpty()) {
			StorageTableDTO storageTable = new StorageTableDTO();
			this.storageForms.stream().findAny().ifPresent(s -> {
				storageTable.setMeasureUnit(s.getUnitAmount().getMeasureUnit());
				storageTable.setContainerWeight(s.getContainerWeight());
				BasicValueEntity<Warehouse> warehouse = s.getWarehouseLocation();
				if(warehouse != null)
					storageTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
			});
			List<BasicStorageDTO> amounts = this.storageForms.stream().map((s) -> {
				return new BasicStorageDTO(s.getId(), s.getVersion(), s.getOrdinal(), s.getNumberUnits());
			}).collect(Collectors.toList());
			storageTable.setAmounts(amounts);
			return storageTable;
		}
		return null;
	}
	
//	/**
//	 * @return the total balance in lots (lot = 35,000lbs)
//	 */
//	public AmountWithUnit getTotalLots() {
//		AmountWithUnit totalBalanceAmount = getTotalBalanceAmount();
//		if(totalBalanceAmount == null) {
//			return null;
//		}
//		return new AmountWithUnit(
//				MeasureUnit.convert(totalBalanceAmount, MeasureUnit.LOT)
//				.setScale(MeasureUnit.SCALE, RoundingMode.HALF_DOWN), MeasureUnit.LOT);
//	}
	
	/**
	 * Transforms List of InventoryProcessItemWithStorage as fetched from db,
	 * to List of ProcessItemInventoryRows as used for view
	 * @param processItemWithStorages
	 * @return
	 */
	public static List<ProcessItemInventory> getProcessItemInventoryRows(
			List<InventoryProcessItemWithStorage> processItemWithStorages) {
		Map<ProcessItemInventory, List<StorageInventoryRow>> processItemStorageMap = processItemWithStorages
			.stream()
			.collect(Collectors.groupingBy(
					InventoryProcessItemWithStorage::getProcessItemInventoryRow, 
					LinkedHashMap::new, 
					Collectors.mapping(InventoryProcessItemWithStorage::getStorageInventoryRow,
							Collectors.toList())));
		
		List<ProcessItemInventory> processItemInventoryRow = new ArrayList<ProcessItemInventory>();
		processItemStorageMap.forEach((k, v) -> {
			k.setStorageForms(v);
			processItemInventoryRow.add(k);
		});
		
		return processItemInventoryRow;
	}
	
	
}
