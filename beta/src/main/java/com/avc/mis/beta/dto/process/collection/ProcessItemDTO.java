/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.BaseEntityDTO;
import com.avc.mis.beta.dto.process.inventory.BasicStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageBaseDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageTableDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.OrderItem;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.process.collection.ReceiptItem;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class ProcessItemDTO extends ProcessGroupDTO implements ListGroup<StorageDTO> {

	private ItemWithUnitDTO item; //change to itemDTO in order to get category
	private MeasureUnit measureUnit;
	private String description;
	private String remarks;
	
//	@Setter(value = AccessLevel.PROTECTED) @Getter(value = AccessLevel.NONE)
	private List<StorageDTO> storageForms;
	
//	private AmountWithUnit[] totalAmount;
	
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
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem);
		this.item = new ItemWithUnitDTO(processItem.getItem());
		this.measureUnit = processItem.getMeasureUnit();
		
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		
		setStorageForms(processItem.getStorageForms().stream()
				.map(i->{return new StorageDTO(i);})
				.collect(Collectors.toList()));

		
	}

	public ProcessItemDTO(Integer id, Integer version, Integer ordinal,
			ItemWithUnitDTO item, MeasureUnit measureUnit,
			String groupName, String description, String remarks, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
		this.item = item;
		this.measureUnit = measureUnit;
		this.description = description;
		this.remarks = remarks;
	}
	
//	public void setStorageForms(List<StorageBaseDTO> storageForms) {
//		this.storageForms = storageForms;
//	}
	
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
		
//		BigDecimal accessWeight = storageTable.getAccessWeight();
		BasicValueEntity<Warehouse> warehouse = storageTable.getWarehouseLocation();
		List<StorageDTO> storageForms = new ArrayList<StorageDTO>();
		for(BasicStorageDTO amount: storageTable.getAmounts()) {
			StorageDTO storage = new StorageDTO();
			storageForms.add(storage);
			storage.setId(amount.getId());
			storage.setVersion(amount.getVersion());
			storage.setOrdinal(amount.getOrdinal());
			storage.setNumberUnits(amount.getAmount());
//			storage.setAccessWeight(accessWeight);
			storage.setWarehouseLocation(warehouse);
		}
		setStorageForms(storageForms);
		
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
//				storageTable.setMeasureUnit(s.getUnitAmount().getMeasureUnit());
//				storageTable.setAccessWeight(s.getAccessWeight());
//				BasicValueEntity<Warehouse> warehouse = s.getWarehouseLocation();
//				if(warehouse != null)
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
		
//		if(this.item.getGroup() == ItemGroup.PRODUCT && MeasureUnit.WEIGHT_UNITS.contains(totalAmount.getMeasureUnit())) {
//			return AmountWithUnit.weightDisplay(totalAmount, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
//		}
		return Arrays.asList(totalAmount.setScale(MeasureUnit.SUM_DISPLAY_SCALE));

	}

	@JsonIgnore
	@Override
	public void setList(List<StorageDTO> list) {
		setStorageForms(list);
//		setStorageForms((List<StorageDTO>) list.stream().collect(Collectors.toCollection(ArrayList<StorageDTO>::new)));
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
		else if(getStorageForms() == null || getStorageForms().isEmpty()) {
			throw new IllegalArgumentException("Process line has to contain at least one storage line");
		}
		else {
			Ordinal.setOrdinals(getStorageForms());
			processItem.setStorageForms(this.storageForms.stream().map(i -> i.fillEntity(new Storage())).collect(Collectors.toSet()));
		}
		
		return processItem;
	}

		
}
