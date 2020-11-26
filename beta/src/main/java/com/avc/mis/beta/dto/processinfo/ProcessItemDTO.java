/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageTableDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessItemDTO extends ProcessGroupDTO {

	private ItemDTO item; //change to itemDTO in order to get category
	private MeasureUnit measureUnit;
	private String description;
	private String remarks;
	
	private List<StorageDTO> storageForms;
	
//	private AmountWithUnit[] totalAmount;
	
	public ProcessItemDTO(Integer id, Integer version, Integer ordinal, 
			Integer itemId, String itemValue, ProductionUse productionUse, Class<? extends Item> clazz,
			MeasureUnit measureUnit,
			String groupName, String description, String remarks, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
		this.item = new ItemDTO(itemId, itemValue, null, null, productionUse, clazz);
		this.measureUnit = measureUnit;
		this.description = description;
		this.remarks = remarks;
		
	}
	
	
	/**
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem);
		this.item = new ItemDTO(processItem.getItem());
		this.measureUnit = processItem.getMeasureUnit();
		
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		
		setStorageForms(Arrays.stream(processItem.getStorageForms())
				.map(i->{return new StorageDTO(i);})
				.collect(Collectors.toList()));

		
	}

	public ProcessItemDTO(Integer id, Integer version, Integer ordinal,
			ItemDTO item, MeasureUnit measureUnit,
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
				storageTable.setContainerWeight(s.getContainerWeight());
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
	
	public AmountWithUnit[] getTotalAmount() {
		BigDecimal total = this.storageForms.stream()
				.map(sf -> sf.getTotal())
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		
		return new AmountWithUnit[] {
				totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)
		};
		
	}

	
	
	public static List<ProcessItemDTO> getProcessItems(List<ProcessItemWithStorage> itemWithStorages) {
		Map<Integer, List<ProcessItemWithStorage>> map = itemWithStorages.stream()
				.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, LinkedHashMap::new, Collectors.toList()));
		List<ProcessItemDTO> processItems = new ArrayList<>();
		for(List<ProcessItemWithStorage> list: map.values()) {
			ProcessItemDTO processItem = list.get(0).getProcessItem();
			List<StorageDTO> storages = list.stream().map(i -> i.getStorage()).collect(Collectors.toList());
			storages.sort(Ordinal.ordinalComparator());
			processItem.setStorageForms(storages);
			processItems.add(processItem);
		}
//		processItems.sort(Ordinal.ordinalComparator());
		return processItems;
	}
		
}
