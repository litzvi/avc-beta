/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemDTO;
import com.avc.mis.beta.dto.process.inventory.UsedItemTableDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
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
				usedItemTable.setAccessWeight(storage.getAccessWeight());
				BasicValueEntity<Warehouse> warehouse = storage.getWarehouseLocation();
				if(warehouse != null)
					usedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
			});
			List<BasicUsedStorageDTO> used = this.usedItems.stream().map((i) -> {
				StorageDTO storage = i.getStorage();
				return new BasicUsedStorageDTO(i.getId(), i.getVersion(), 
						storage.getId(), storage.getVersion(), storage.getOrdinal(), storage.getNumberUnits());
			}).collect(Collectors.toList());
			usedItemTable.setAmounts(used);
			return usedItemTable;
		}
		return null;
	}
	
	//not matched for packed items
//	public AmountWithUnit[] getTotalAmount() {
//		AmountWithUnit totalAmount = usedItems.stream()
//				.map(ui -> {
//					StorageDTO storage = ui.getStorage();
//					return new AmountWithUnit(storage.getUnitAmount()
//							.subtract(Optional.ofNullable(storage.getContainerWeight()).orElse(BigDecimal.ZERO))
//							.multiply(ui.getNumberUsedUnits()), 
//						ui.getMeasureUnit());
//				})
//				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//		return new AmountWithUnit[] {totalAmount.setScale(MeasureUnit.SCALE),
//				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
//	}
	
	@JsonIgnore
	@Override
	public void setList(List<UsedItemDTO> list) {
		setUsedItems(list);
	}
}
