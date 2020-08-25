/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.query.UsedItemWithGroup;
import com.avc.mis.beta.dto.values.BasicValueEntity;
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
			usedItemTable.setGroupName(this.groupName);
			this.usedItems.stream().findAny().ifPresent(s -> {
				usedItemTable.setItem(s.getItem());
				usedItemTable.setItemPo(s.getItemPo());
				usedItemTable.setMeasureUnit(s.getUnitAmount().getMeasureUnit());
				usedItemTable.setContainerWeight(s.getContainerWeight());
				BasicValueEntity<Warehouse> warehouse = s.getWarehouseLocation();
				usedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
			});
			List<BasicUsedStorageDTO> amounts = this.usedItems.stream().map((s) -> {
				return new BasicUsedStorageDTO(s.getId(), s.getVersion(), s.getOrdinal(), s.getUnitAmount().getAmount());
			}).collect(Collectors.toList());
			usedItemTable.setAmounts(amounts);
			return usedItemTable;
		}
		return null;
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
