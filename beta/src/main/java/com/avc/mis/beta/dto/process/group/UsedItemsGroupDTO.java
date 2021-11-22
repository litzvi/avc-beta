/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.storages.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.storages.StorageBaseDTO;
import com.avc.mis.beta.dto.process.storages.StorageDTO;
import com.avc.mis.beta.dto.process.storages.UsedItemDTO;
import com.avc.mis.beta.dto.process.storages.UsedItemTableDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.group.UsedItemsGroup;
import com.avc.mis.beta.entities.process.storages.UsedItem;
import com.avc.mis.beta.entities.values.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO that contains a group of used items
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UsedItemsGroupDTO extends ProcessGroupDTO {

	private List<UsedItemDTO> usedItems;


	public UsedItemsGroupDTO(Integer id, Integer version, Integer ordinal,
			String groupName, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
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
					if(MeasureUnit.NONE == ui.getItem().getUnit().getMeasureUnit()) {
						return new AmountWithUnit(ui.getTotal(), ui.getMeasureUnit());
					}
					else {
						return ui.getItem().getUnit().multiply(ui.getTotal());
					}
				})
				.reduce(AmountWithUnit::add).get();
		} catch (NoSuchElementException | UnsupportedOperationException e) {
			return null;
		}
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

