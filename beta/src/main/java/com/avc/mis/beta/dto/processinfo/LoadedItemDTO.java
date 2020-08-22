/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.PoCodeDTO;
import com.avc.mis.beta.dto.query.LoadedItemWithStorage;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.processinfo.LoadedItem;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoadedItemDTO extends ProcessItemDTO {
	
	private PoCodeDTO poCode;
		
	public LoadedItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			String description, String remarks, boolean tableView,
			Integer poCodeId, String contractTypeCode, String contractTypeSuffix, String supplierName) {
		super(id, version, itemId, itemValue, description, remarks, tableView);
		this.poCode = new PoCodeDTO(poCodeId, contractTypeCode, contractTypeSuffix, supplierName);		
	}	
	
	/**
	 * @param processItem
	 */
	public LoadedItemDTO(LoadedItem loadedItem) {
		super(loadedItem);
		this.poCode = new PoCodeDTO(loadedItem.getPoCode());
	}

	public LoadedItemDTO(Integer id, Integer version,
			BasicValueEntity<Item> item, PoCodeDTO poCode,
			String description, String remarks) {
		super(id, version, item, description, remarks);
		this.poCode = poCode;
	}

	public static List<LoadedItemDTO> getLoadedItems(List<LoadedItemWithStorage> storages) {
		Map<Integer, List<LoadedItemWithStorage>> map = storages.stream()
				.collect(Collectors.groupingBy(LoadedItemWithStorage::getId, Collectors.toList()));
		List<LoadedItemDTO> loadedItems = new ArrayList<>();
		for(List<LoadedItemWithStorage> list: map.values()) {
			LoadedItemDTO loadedItem = list.get(0).getLoadedItem();
			loadedItem.setStorageForms(list.stream().map(i -> i.getStorage())
					.collect(Collectors.toCollection(() -> new TreeSet<StorageDTO>(Ordinal.ordinalComparator()))));
			loadedItems.add(loadedItem);
		}
		return loadedItems;
	}
	
}
