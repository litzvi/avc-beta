/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicStorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageTableDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemWithUnitDTO;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.item.Item;
import com.avc.mis.beta.entities.item.ItemGroup;
import com.avc.mis.beta.entities.item.ProductionUse;
import com.avc.mis.beta.entities.process.collection.ProcessItem;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ProcessItemDTO extends ProcessGroupDTO implements ListGroup<StorageDTO> {

	private ItemWithUnitDTO item; //change to itemDTO in order to get category
	private MeasureUnit measureUnit;
	private String description;
	private String remarks;
	
	private List<StorageDTO> storageForms;
	
//	private AmountWithUnit[] totalAmount;
	
	public ProcessItemDTO(Integer id, Integer version, Integer ordinal, 
			Integer itemId, String itemValue, ProductionUse productionUse, 
			AmountWithUnit itemUnit, Class<? extends Item> clazz,
			MeasureUnit measureUnit,
			String groupName, String description, String remarks, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
		this.item = new ItemWithUnitDTO(itemId, itemValue, null, null, productionUse, itemUnit, clazz);
		this.measureUnit = measureUnit;
		this.description = description;
		this.remarks = remarks;
		
	}
	
	
	/**
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem);
		this.item = new ItemWithUnitDTO(processItem.getItem());
		this.measureUnit = processItem.getMeasureUnit();
		
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		
		setStorageForms(Arrays.stream(processItem.getStorageForms())
				.map(i->{return new StorageDTO(i);})
				.collect(Collectors.toList()));

		
	}

	public ProcessItemDTO(Integer id, Integer version, Integer ordinal,
			ItemWithUnitDTO item, MeasureUnit measureUnit,
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
//				storageTable.setAccessWeight(s.getAccessWeight());
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
	
	public List<AmountWithUnit> getTotalAmount() {		
		BigDecimal total = this.storageForms.stream()
				.map(sf -> sf.getTotal())
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		AmountWithUnit totalAmount;
		Class<? extends Item> itemClass = this.item.getClazz();
		if(MeasureUnit.NONE == item.getUnit().getMeasureUnit()) {
			totalAmount = new AmountWithUnit(total, this.measureUnit);
		}
		else {
			totalAmount = this.item.getUnit().multiply(total);
		}
//		else if(itemClass == PackedItem.class){
//			totalAmount = this.item.getUnit().multiply(total);
//		}
//		else 
//		{
//			throw new IllegalStateException("The class can only apply to weight items");
//		}
		
		if(this.item.getGroup() == ItemGroup.PRODUCT && MeasureUnit.WEIGHT_UNITS.contains(totalAmount.getMeasureUnit())) {
			return AmountWithUnit.weightDisplay(totalAmount, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
		}
		return Arrays.asList(totalAmount);

	}

	
	/**
	 * static function for building List of ProcessItemDTO from a List of ProcessItemWithStorage
	 * received by a join query of storages with their processItem.
	 * @param itemWithStorages a List<ProcessItemWithStorage>
	 * @return List<ProcessItemDTO> as in the DTO structure.
	 */
//	public static List<ProcessItemDTO> getProcessItems(List<ProcessItemWithStorage> itemWithStorages) {
//		Map<Integer, List<ProcessItemWithStorage>> map = itemWithStorages.stream()
//				.collect(Collectors.groupingBy(ProcessItemWithStorage::getId, LinkedHashMap::new, Collectors.toList()));
//		List<ProcessItemDTO> processItems = new ArrayList<>();
//		for(List<ProcessItemWithStorage> list: map.values()) {
//			ProcessItemDTO processItem = list.get(0).getProcessItem();
////			List<StorageDTO> storages = list.stream().map(i -> i.getStorage()).collect(Collectors.toList());
////			storages.sort(Ordinal.ordinalComparator());
////			processItem.setStorageForms(storages);
//			processItem.setStorageForms(list.stream()
//					.map(i -> i.getStorage())
////					.sorted(Ordinal.ordinalComparator()) // done in query
//					.collect(Collectors.toList()));
//
//			processItems.add(processItem);
//		}
////		processItems.sort(Ordinal.ordinalComparator());
//		return processItems;
//	}


	@JsonIgnore
	@Override
	public void setList(List<StorageDTO> list) {
		setStorageForms(list);
//		setStorageForms((List<StorageDTO>) list.stream().collect(Collectors.toCollection(ArrayList<StorageDTO>::new)));
	}



		
}
