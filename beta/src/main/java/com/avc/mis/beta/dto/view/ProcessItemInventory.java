/**
 * 
 */
package com.avc.mis.beta.dto.view;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.avc.mis.beta.dto.BasicDTO;
import com.avc.mis.beta.dto.process.inventory.BasicStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageTableDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.dto.values.PoCodeBasic;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class ProcessItemInventory extends BasicDTO implements ListGroup<StorageInventoryRow> {

	private ItemWithUnitDTO item;
	private MeasureUnit measureUnit;
	private PoCodeBasic poCode;
	private String[] poCodes;
	private OffsetDateTime itemProcessDate;
	private OffsetDateTime receiptDate;
//	private List<AmountWithUnit> totalBalanceAmount; //not used now
	
	@JsonIgnore
	private boolean tableView;
	private List<StorageInventoryRow> storageForms;
	
	/**
	 * All database fields (the fields in the form they are fetched from the db) arguments constructor, 
	 * excluding list of storage forms and calculated totals.
	 */
	public ProcessItemInventory(Integer id, 
			Integer itemId, String itemValue, MeasureUnit itemMeasureUnit, ItemGroup itemGroup, 
			BigDecimal unitAmount, MeasureUnit itemUnitMeasureUnit, Class<? extends Item> clazz,
			MeasureUnit processItemMeasureUnit, 
			Integer poCodeId, String poCodeCode, String contractTypeCode, String contractTypeSuffix, String supplierName, 
			String poCodes, 
			OffsetDateTime processDate, OffsetDateTime receiptDate, boolean tableView) {
		super(id);
		this.item = new ItemWithUnitDTO(itemId, itemValue, itemMeasureUnit, itemGroup, null, unitAmount, itemUnitMeasureUnit, clazz);
		this.measureUnit = processItemMeasureUnit;
		this.poCode = new PoCodeBasic(poCodeId, poCodeCode, contractTypeCode, contractTypeSuffix, supplierName);
		if(poCodes != null)
			this.poCodes = Stream.of(poCodes.split(",")).distinct().toArray(String[]::new);
		this.itemProcessDate = processDate;
		this.receiptDate = receiptDate;
		this.tableView = tableView;
	}
		
	/**
	 * Setter for storageForms, sets the storages for this process item 
	 * and updates total balance accordingly.
	 * @param storageForms List of StorageInventoryRow
	 */
	//not used now
//	public void setStorageForms(List<StorageInventoryRow> storageForms) {
//		this.storageForms = storageForms;
//		if(storageForms.size() > 0) {
//			AmountWithUnit totalBalanceAmount = storageForms.stream()
//					.map(StorageInventoryRow::getTotalBalance)
//					.reduce(AmountWithUnit::add).get();
//			this.totalBalanceAmount = AmountWithUnit.amountDisplay(
//					totalBalanceAmount, this.item, Arrays.asList(totalBalanceAmount.getMeasureUnit(), MeasureUnit.LOT));
//		}
//		else {
//			this.totalBalanceAmount = null;
//		}
//		
//	}
	
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
//				storageTable.setMeasureUnit(s.getUnitAmount().getMeasureUnit());
				storageTable.setAccessWeight(s.getAccessWeight());
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
	
	@JsonIgnore
	@Override
	public void setList(List<StorageInventoryRow> list) {
		setStorageForms(list);
	}
	
	
}
