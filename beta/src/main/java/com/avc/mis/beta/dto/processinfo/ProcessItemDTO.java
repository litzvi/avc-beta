/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.dto.data.EmailDTO;
import com.avc.mis.beta.dto.data.PhoneDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.enums.SupplyGroup;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.values.Item;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessItemDTO extends ProcessDTO {

	private ItemDTO item; //change to itemDTO in order to get category
//	private PoCodeDTO itemPo;
	
//	BigDecimal unitAmount;
//	MeasureUnit measureUnit;
//	BigDecimal numberUnits;	
//	Warehouse storageLocation;
	private String description;
	private String remarks;
	
	private boolean tableView;
	private SortedSet<StorageDTO> storageForms = new TreeSet<>(Ordinal.ordinalComparator());
	
	public ProcessItemDTO(Integer id, Integer version, Integer itemId, String itemValue, 
			ItemCategory itemCategory,
			/* Integer poCodeId, ContractTypeCode contractTypeCode, String supplierName, */
			/*BigDecimal unitAmount, MeasureUnit measureUnit, BigDecimal numberUnits, Warehouse storageLocation, */
			String description, String remarks, boolean tableView) {
		super(id, version);
		this.item = new ItemDTO(itemId, itemValue, null, null, itemCategory);
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
		this.tableView = tableView;
		
//		this.unitAmount.setScale(3);//for testing with assertEquals
//		this.numberUnits.setScale(3);//for testing with assertEquals
		
	}
	
	
	/**
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem.getId(), processItem.getVersion());
		this.item = new ItemDTO(processItem.getItem());
//		if(processItem.getItemPo() != null)
//			this.itemPo = new PoCodeDTO(processItem.getItemPo());
//		else
//			this.itemPo = null;
		
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		this.tableView = processItem.isTableView();
		
		this.storageForms.addAll(Arrays.stream(processItem.getStorageForms())
				.map(i->{return new StorageDTO(i);})
				.collect(Collectors.toSet()));

		
	}

	public ProcessItemDTO(Integer id, Integer version,
			ItemDTO item, /* PoCodeDTO itemPo, */
			String description, String remarks) {
		super(id, version);
		this.item = item;
//		this.itemPo = itemPo;
		this.description = description;
		this.remarks = remarks;
	}
	
	public void setStorageForms(Collection<StorageDTO> storageForms) {
		this.storageForms.addAll(storageForms);
	}
	
	public Set<StorageDTO> getStorageForms() {
		if(tableView) {
			return null;
		}
		return this.storageForms;
	}
	
	public StorageTableDTO getStorage() {
		if(tableView && this.storageForms != null && !this.storageForms.isEmpty()) {
			StorageTableDTO storageTable = new StorageTableDTO();
			this.storageForms.stream().findAny().ifPresent(s -> {
				storageTable.setMeasureUnit(s.getUnitAmount().getMeasureUnit());
				storageTable.setContainerWeight(s.getContainerWeight());
				BasicValueEntity<Warehouse> warehouse = s.getWarehouseLocation();
				storageTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
			});
			List<BasicStorageDTO> amounts = this.storageForms.stream().map((s) -> {
				return new BasicStorageDTO(s.getId(), s.getVersion(), s.getOrdinal(), s.getUnitAmount().getAmount());
			}).collect(Collectors.toList());
			storageTable.setAmounts(amounts);
			return storageTable;
		}
		return null;
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
			processItem.setStorageForms(list.stream().map(i -> i.getStorage())
					.collect(Collectors.toList()));
			processItems.add(processItem);
		}
		return processItems;
	}
	
//	public static List<PoProcessItemEntry> getProcessItemsWithPo(List<ProcessItemWithStorage> storages) {
//		Map<Integer, List<ProcessItemWithStorage>> map = storages.stream()
//				.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, Collectors.toList()));
//		List<PoProcessItemEntry> processItems = new ArrayList<>();
//		for(List<ProcessItemWithStorage> list: map.values()) {
//			ProcessItemDTO processItem = list.get(0).getProcessItem();
//			processItem.setStorageForms(list.stream().map(i -> i.getStorage()).collect(Collectors.toSet()));
//			PoProcessItemEntry processItemEntry = 
//					new PoProcessItemEntry(list.get(0).getPo(), processItem);
//			processItems.add(processItemEntry);
//		}
//		return processItems;
//	}
	
	
	
}
