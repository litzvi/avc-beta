/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.query.UsedItemWithGroup;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.UsedItemsGroup;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsedItemsGroupDTO extends ProcessDTO {

	private String groupName;

	@JsonIgnore
	private boolean tableView;
	private Set<UsedItemDTO> usedItems; //can use a SortedSet like ContactDetails to maintain order


	public UsedItemsGroupDTO(Integer id, Integer version,
			String groupName, boolean tableView) {
		super(id, version);
		this.groupName = groupName;
		this.tableView = tableView;
	}	

	public UsedItemsGroupDTO(UsedItemsGroup group) {
		super(group.getId(), group.getVersion());
		this.groupName = group.getGroupName();
		this.tableView = group.isTableView();
		this.usedItems = (Arrays.stream(group.getUsedItems())
				.map(u->{return new UsedItemDTO(u);})
				.collect(Collectors.toSet()));
	}
	
	public Set<UsedItemDTO> getUsedItems() {
		if(tableView) {
			return null;
		}
		return this.usedItems;
	}
	
	public UsedItemTableDTO getUsedItem() {
		if(tableView && this.usedItems != null && !this.usedItems.isEmpty()) {
			UsedItemTableDTO usedItemTable = new UsedItemTableDTO();
			this.usedItems.stream().findAny().ifPresent(s -> {
				usedItemTable.setItem(s.getItem());
				usedItemTable.setItemPo(s.getItemPo());
				StorageDTO storage = s.getStorage();
				usedItemTable.setMeasureUnit(storage.getUnitAmount().getMeasureUnit());
				usedItemTable.setContainerWeight(storage.getContainerWeight());
				BasicValueEntity<Warehouse> warehouse = storage.getWarehouseLocation();
				if(warehouse != null)
					usedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
			});
			List<BasicUsedStorageDTO> used = this.usedItems.stream().map((s) -> {
				StorageDTO storage = s.getStorage();
				return new BasicUsedStorageDTO(s.getId(), s.getVersion(), 
						storage.getId(), storage.getVersion(), storage.getOrdinal(), storage.getUnitAmount().getAmount());
			}).collect(Collectors.toList());
			usedItemTable.setAmounts(used);
			return usedItemTable;
		}
		return null;
	}
	
	public AmountWithUnit[] getTotalAmount() {
		AmountWithUnit totalAmount = usedItems.stream()
				.map(ui -> ui.getStorage().getUnitAmount()
						.substract(Optional.ofNullable(ui.getStorage().getContainerWeight()).orElse(BigDecimal.ZERO))
						.multiply(ui.getNumberUnits()))
				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
		return new AmountWithUnit[] {totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}
	
	public static List<UsedItemsGroupDTO> getUsedItemsGroups(List<UsedItemWithGroup> usedItems) {
		Map<Integer, List<UsedItemWithGroup>> map = usedItems.stream()
				.collect(Collectors.groupingBy(UsedItemWithGroup::getId, Collectors.toList()));
		List<UsedItemsGroupDTO> usedItemsGroups = new ArrayList<>();
		for(List<UsedItemWithGroup> list: map.values()) {
			UsedItemsGroupDTO usedItemsGroup = list.get(0).getUsedItemsGroup();
			usedItemsGroup.setUsedItems(list.stream().map(i -> i.getUsedItem())
					.collect(Collectors.toSet()));
			usedItemsGroups.add(usedItemsGroup);
		}
		return usedItemsGroups;
	}
}
