/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageBaseDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemTableDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.collection.UsedItemsGroup;
import com.avc.mis.beta.entities.process.inventory.UsedItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UsedItemsGroupDTO extends ProcessGroupDTO 
//implements ListGroup<UsedItemDTO> 
{

	private List<UsedItemDTO> usedItems;


	public UsedItemsGroupDTO(Integer id, Integer version, Integer ordinal,
			String groupName, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
	}	

	public UsedItemsGroupDTO(UsedItemsGroup group) {
		super(group);
		this.usedItems = (group.getUsedItems().stream()
				.map(u->{return new UsedItemDTO(u);})
				.collect(Collectors.toList()));
	}
	
	/**
	 * Setter for adding list of Used items that share the same common measure unit, 
	 * empty container weight and each only have one unit, that are usually represented as a table in ProcessItem.
	 * Used in order to match used storages from a ProcessItem that was set by corresponding setStorage(storageTable) in ProcessItem.
	 * Usefully presented in a table or list of only ordinal (number) and amount, since they all share all other parameters.
	 * @param usedItemTable
	 */
	public void setUsedItem(UsedItemTableDTO usedItemTable) {
			
		setTableView(true);
		
		List<BasicUsedStorageDTO> basicUsedStorages = usedItemTable.getAmounts();
		List<UsedItemDTO> usedItems = new ArrayList<UsedItemDTO>();
		for(BasicUsedStorageDTO basicUsedStorage: basicUsedStorages) {
			UsedItemDTO usedItem = new UsedItemDTO();
			usedItems.add(usedItem);
			usedItem.setId(basicUsedStorage.getId());
			usedItem.setVersion(basicUsedStorage.getVersion());
			usedItem.setNumberUsedUnits(basicUsedStorage.getAmount());
			StorageDTO storage = new StorageDTO();
			storage.setId(basicUsedStorage.getStorageId());
			storage.setVersion(basicUsedStorage.getStorageVersion());
			usedItem.setStorage(storage);
			
			
		}
		setUsedItems(usedItems);
	}
	
	public List<UsedItemDTO> getUsedItems() {
		if(isTableView()) {
			return null;
		}
		return this.usedItems;
	}
	
	public UsedItemTableDTO getUsedItem() {
		if(isTableView() && this.usedItems != null && !this.usedItems.isEmpty()) {
			UsedItemTableDTO usedItemTable = new UsedItemTableDTO();
			this.usedItems.stream().findAny().ifPresent(ui -> {
				usedItemTable.setItem(ui.getItem());
				usedItemTable.setMeasureUnit(ui.getMeasureUnit());
				usedItemTable.setItemPo(ui.getItemPo());
				usedItemTable.setItemProcessDate(ui.getItemProcessDate());
				StorageBaseDTO storage = ui.getStorage();
//				usedItemTable.setAccessWeight(storage.getAccessWeight());
//				BasicValueEntity<Warehouse> warehouse = storage.getWarehouseLocation();
//				if(warehouse != null)
//					usedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				usedItemTable.setWarehouseLocation(storage.getWarehouseLocation());
				
				usedItemTable.setItemPoCodes(ui.getItemPoCodes());
				usedItemTable.setItemSuppliers(ui.getItemSuppliers());
				usedItemTable.setCashewGrade(ui.getCashewGrade());
			});
			List<BasicUsedStorageDTO> used = this.usedItems.stream().map((i) -> {
				StorageBaseDTO storage = i.getStorage();
				return new BasicUsedStorageDTO(i.getId(), i.getVersion(), 
						storage.getId(), storage.getVersion(), storage.getOrdinal(), storage.getNumberUnits(), 
						i.getNumberUsedUnits(), i.getNumberAvailableUnits());
			}).collect(Collectors.toList());
			usedItemTable.setAmounts(used);
			return usedItemTable;
		}
		return null;
	}
	
	public List<AmountWithUnit> getTotalAmount() {
		if(usedItems == null || usedItems.isEmpty()) {
			return null;
		}
		AmountWithUnit totalAmount;
		try {
			totalAmount = usedItems.stream()
				.map(ui -> {
//					Class<? extends Item> itemClass = ui.getItem().getClazz();
					if(MeasureUnit.NONE == ui.getItem().getUnit().getMeasureUnit()) {
						return new AmountWithUnit(ui.getTotal(), ui.getMeasureUnit());
					}
					else {
						return ui.getItem().getUnit().multiply(ui.getTotal());
					}
//					else if(itemClass == PackedItem.class){
//						return ui.getItem().getUnit().multiply(ui.getTotal());
//					}
//					else 
//					{
//						throw new IllegalStateException("Unknowen item class");
//					}
				})
				.reduce(AmountWithUnit::add).get();
		} catch (NoSuchElementException | UnsupportedOperationException e) {
			return null;
		}
//		if(!MeasureUnit.WEIGHT_UNITS.contains(totalAmount.getMeasureUnit())) {
//			return Arrays.asList(totalAmount);
//		}
//		return AmountWithUnit.weightDisplay(totalAmount, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
		return Arrays.asList(totalAmount.setScale(MeasureUnit.SUM_DISPLAY_SCALE));
	}
	
	@JsonIgnore
	public AmountWithUnit getTotalWeight() {
		if(usedItems == null || usedItems.isEmpty()) {
			return null;
		}
		Optional<AmountWithUnit> totalWeight;
		try {
			totalWeight = usedItems.stream()
				.map(ui -> {
					Class<? extends Item> itemClass = ui.getItem().getClazz();
					if(MeasureUnit.NONE == ui.getItem().getUnit().getMeasureUnit() && MeasureUnit.WEIGHT_UNITS.contains(ui.getMeasureUnit())) {
						return new AmountWithUnit(ui.getTotal(), ui.getMeasureUnit());
					}
					else if(MeasureUnit.WEIGHT_UNITS.contains(ui.getItem().getUnit().getMeasureUnit())){
						return ui.getItem().getUnit().multiply(ui.getTotal());
					}
					else 
					{
						return null;
					}
				})
				.filter(i -> i != null)
				.reduce(AmountWithUnit::add);
		} catch (NoSuchElementException | UnsupportedOperationException e) {
			return null;
		}
		
		return totalWeight.orElse(null);
	}
	
//	@JsonIgnore
//	@Override
//	public void setList(List<UsedItemDTO> list) {
//		setUsedItems(list);
//	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return UsedItemsGroup.class;
	}
	
	@Override
	public UsedItemsGroup fillEntity(Object entity) {
		UsedItemsGroup usedItemsGroup;
		if(entity instanceof UsedItemsGroup) {
			usedItemsGroup = (UsedItemsGroup) entity;
		}
		else {
			throw new IllegalStateException("Param has to be UsedItemsGroup class");
		}
		super.fillEntity(usedItemsGroup);
		if(this.usedItems == null || this.usedItems.isEmpty()) {
			throw new IllegalArgumentException("UsedItemGroup has to containe at least one used item");
		}
		else {
			Ordinal.setOrdinals(this.usedItems);
			usedItemsGroup.setUsedItems(this.usedItems.stream().map(i -> i.fillEntity(new UsedItem())).collect(Collectors.toSet()));
		}
		
		return usedItemsGroup;
	}
}

