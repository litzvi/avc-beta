/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.queryRows.PoProcessItemEntry;
import com.avc.mis.beta.dto.queryRows.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.values.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessItemDTO extends ProcessDTO {

	private BasicValueEntity<Item> item;
//	private PoCodeDTO itemPo;
	
//	BigDecimal unitAmount;
//	MeasureUnit measureUnit;
//	BigDecimal numberUnits;	
//	Warehouse storageLocation;
	private String description;
	private String remarks;
	
	private Set<StorageDTO> storageForms; //can use a SortedSet like ContactDetails to maintain order
	
	public ProcessItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			/*BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, Warehouse storageLocation, */
			String description, String remarks) {
		super(id, version);
		this.item = new BasicValueEntity<Item>(itemId, itemValue);
//		if(poCodeId != null)
//			this.itemPo = new PoCodeDTO(poCodeId, contractTypeCode, supplierName);
//		else
//			this.itemPo = null;
//		if(itemPo != null)
//			this.itemPo = new PoCodeBasic(itemPo);
//		else
//			this.itemPo = null;
		
//		this.unitAmount = unitAmount.setScale(3);
//		this.measureUnit = measureUnit;
//		this.numberUnits = numberUnits.setScale(3);
//		this.storageLocation = storageLocation;
		this.description = description;
		this.remarks = remarks;
		
//		this.unitAmount.setScale(3);//for testing with assertEquals
//		this.numberUnits.setScale(3);//for testing with assertEquals
		
	}
	
	
	/**
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem.getId(), processItem.getVersion());
		this.item = new BasicValueEntity<Item>(processItem.getItem());
//		if(processItem.getItemPo() != null)
//			this.itemPo = new PoCodeDTO(processItem.getItemPo());
//		else
//			this.itemPo = null;
		
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		
		this.storageForms = Arrays.stream(processItem.getStorageForms())
				.map(i->{return new StorageDTO(i);}).collect(Collectors.toSet());

		
	}

	public ProcessItemDTO(Integer id, Integer version,
			BasicValueEntity<Item> item, /* PoCodeDTO itemPo, */
			String description, String remarks) {
		super(id, version);
		this.item = item;
//		this.itemPo = itemPo;
		this.description = description;
		this.remarks = remarks;
	}

	public Optional<AmountWithUnit> getTotalAmount() {
		return storageForms.stream()
				.map(sf -> sf.getUnitAmount().multiply(sf.getNumberUnits()))
				.reduce(AmountWithUnit::add);
	}
	
	public static List<ProcessItemDTO> getProcessItems(List<ProcessItemWithStorage> storages) {
		Map<Integer, List<ProcessItemWithStorage>> map = storages.stream()
				.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, Collectors.toList()));
		List<ProcessItemDTO> processItems = new ArrayList<>();
		for(List<ProcessItemWithStorage> list: map.values()) {
			ProcessItemDTO processItem = list.get(0).getProcessItem();
			processItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
			processItems.add(processItem);
		}
		return processItems;
	}
	
	public static List<PoProcessItemEntry> getProcessItemsWithPo(List<ProcessItemWithStorage> storages) {
		Map<Integer, List<ProcessItemWithStorage>> map = storages.stream()
				.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, Collectors.toList()));
		List<PoProcessItemEntry> processItems = new ArrayList<>();
		for(List<ProcessItemWithStorage> list: map.values()) {
			ProcessItemDTO processItem = list.get(0).getProcessItem();
			processItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
			PoProcessItemEntry processItemEntry = 
					new PoProcessItemEntry(list.get(0).getPo(), processItem);
			processItems.add(processItemEntry);
		}
		return processItems;
	}
	
	
	
}
