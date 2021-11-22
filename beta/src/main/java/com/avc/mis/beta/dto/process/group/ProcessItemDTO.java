/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.basic.BasicValueEntity;
import com.avc.mis.beta.dto.basic.ItemWithUnitDTO;
import com.avc.mis.beta.dto.process.storages.BasicStorageDTO;
import com.avc.mis.beta.dto.process.storages.StorageDTO;
import com.avc.mis.beta.dto.process.storages.StorageTableDTO;
import com.avc.mis.beta.dto.process.storages.UsedItemDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.ProductionUse;
import com.avc.mis.beta.entities.process.group.ProcessItem;
import com.avc.mis.beta.entities.process.group.ReceiptItem;
import com.avc.mis.beta.entities.process.storages.Storage;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO that contains a group of storages for a given item produced by the owning process.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ProcessItemDTO extends ProcessGroupDTO {

	private ItemWithUnitDTO item;
	private MeasureUnit measureUnit;
	private String description;
	private String remarks;
	
	private List<StorageDTO> storageForms;
		
	public ProcessItemDTO(Integer id, Integer version, Integer ordinal, 
			Integer itemId, String itemValue, ProductionUse productionUse, 
			AmountWithUnit itemUnit, Class<? extends Item> clazz,
			MeasureUnit measureUnit,
			String groupName, String description, String remarks, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
		this.item = new ItemWithUnitDTO(itemId, itemValue, null, null, productionUse, itemUnit, clazz);
		this.measureUnit = measureUnit;
		this.description = description;
		this.remarks = remarks;
		
	}
	
	/**
	 * Setter for adding Storage forms for items that are processed (used items), 
	 * the new storages will have the same form as the used ones with the given new location.
	 * receives an array (which can be ordered, for later use to add an order to the items).
	 * Filters the not legal items and set needed references to satisfy needed foreign keys of database.
	 * @param usedItems array of UsedItemDTOs that are used
	 */
	public void setUsedItems(Set<UsedItemDTO> usedItems) {
		try {
			setStorageForms(usedItems.stream()
					.map(i -> i.getNewStorage())
					.collect(Collectors.toList()));
		} catch (NullPointerException e) {
			throw new NullPointerException("Used item storage is null");
		}		
	}
	
	/**
	 * Setter for adding list of Storage forms that share the same common measure unit, 
	 * empty container weight and each only have one unit.
	 * Usefully presented in a table or list of only ordinal (number) and amount,
	 * since they all share all other parameters.
	 * @param storageTable
	 */
	public void setStorage(StorageTableDTO storageTable) {
		setTableView(true);
		
		BasicValueEntity<Warehouse> warehouse = storageTable.getWarehouseLocation();
		List<StorageDTO> storageForms = new ArrayList<StorageDTO>();
		for(BasicStorageDTO amount: storageTable.getAmounts()) {
			StorageDTO storage = new StorageDTO();
			storageForms.add(storage);
			storage.setId(amount.getId());
			storage.setVersion(amount.getVersion());
			storage.setOrdinal(amount.getOrdinal());
			storage.setNumberUnits(amount.getAmount());
			storage.setWarehouseLocation(warehouse);
		}
		setStorageForms(storageForms);		
	}
	
	public List<StorageDTO> getStorageFormsField() {
		return this.storageForms;
	}
	
	public List<StorageDTO> getStorageForms() {
		if(isTableView()) {
			return null;
		}
		return this.storageForms;
	}
	
	public StorageTableDTO getStorage() {
		if(isTableView() && this.storageForms != null && !this.storageForms.isEmpty()) {
			StorageTableDTO storageTable = new StorageTableDTO();
			this.storageForms.stream().findAny().ifPresent(s -> {
				storageTable.setWarehouseLocation(s.getWarehouseLocation());
			});
			List<BasicStorageDTO> amounts = this.storageForms.stream().map((s) -> {
				return new BasicStorageDTO(s.getId(), s.getVersion(), s.getOrdinal(), s.getNumberUnits());
			}).collect(Collectors.toList());
			storageTable.setAmounts(amounts);
			return storageTable;
		}
		return null;
	}
	
	public List<AmountWithUnit> getTotalAmount() {		
		BigDecimal total = this.storageForms.stream()
				.map(sf -> sf.getTotal())
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		AmountWithUnit totalAmount;
		Class<? extends Item> itemClass = this.item.getClazz();
		if(MeasureUnit.NONE == item.getUnit().getMeasureUnit()) {
			totalAmount = new AmountWithUnit(total, this.measureUnit);
		}
		else {
			totalAmount = this.item.getUnit().multiply(total);
		}
		
		return Arrays.asList(totalAmount.setScale(MeasureUnit.SUM_DISPLAY_SCALE));

	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return ProcessItem.class;
	}


	@Override
	public ProcessItem fillEntity(Object entity) {
		ProcessItem processItem;
		if(entity instanceof ProcessItem) {
			processItem = (ProcessItem) entity;
		}
		else {
			throw new IllegalStateException("Param has to be ProcessItem class");
		}
		super.fillEntity(processItem);
		if(getItem() != null)
			processItem.setItem(getItem().fillEntity(new Item()));
		processItem.setMeasureUnit(getMeasureUnit());
		processItem.setDescription(getDescription());
		processItem.setRemarks(getRemarks());
		if(processItem instanceof ReceiptItem) {
			
		}
		else if(getStorageFormsField() == null || getStorageFormsField().isEmpty()) {
			throw new IllegalArgumentException("Process line has to contain at least one storage line");
		}
		else {
			Ordinal.setOrdinals(getStorageFormsField());
			processItem.setStorageForms(getStorageFormsField().stream().map(i -> i.fillEntity(new Storage())).collect(Collectors.toSet()));
		}
		
		return processItem;
	}

		
}
