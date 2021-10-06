/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemTableDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.process.collection.UsedItemsGroup;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsedItemsGroupDTO extends ProcessGroupDTO implements ListGroup<UsedItemDTO> {

	private List<UsedItemDTO> usedItems;


	public UsedItemsGroupDTO(Integer id, Integer version, Integer ordinal,
			String groupName, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
	}	

	public UsedItemsGroupDTO(UsedItemsGroup group) {
		super(group);
		this.usedItems = (Arrays.stream(group.getUsedItems())
				.map(u->{return new UsedItemDTO(u);})
				.collect(Collectors.toList()));
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
				StorageDTO storage = ui.getStorage();
//				usedItemTable.setAccessWeight(storage.getAccessWeight());
				BasicValueEntity<Warehouse> warehouse = storage.getWarehouseLocation();
				if(warehouse != null)
					usedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				
				usedItemTable.setItemPoCodes(ui.getItemPoCodes());
				usedItemTable.setItemSuppliers(ui.getItemSuppliers());
				usedItemTable.setCashewGrade(ui.getCashewGrade());
			});
			List<BasicUsedStorageDTO> used = this.usedItems.stream().map((i) -> {
				StorageDTO storage = i.getStorage();
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
	
	@JsonIgnore
	@Override
	public void setList(List<UsedItemDTO> list) {
		setUsedItems(list);
	}
}
