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

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.dto.query.ProcessItemWithStorage;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.ItemDTO;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.ItemCategory;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.ProcessItem;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessItemDTO extends SubjectDataDTO {

	private ItemDTO item; //change to itemDTO in order to get category
	private String groupName;
	private String description;
	private String remarks;
	
	@JsonIgnore
	private boolean tableView;
	private SortedSet<StorageBaseDTO> storageForms;
	
	private AmountWithUnit[] totalAmount;
	
	public ProcessItemDTO(Integer id, Integer version, Integer ordinal, Integer itemId, String itemValue, 
			ItemCategory itemCategory,
			String groupName, String description, String remarks, boolean tableView) {
		super(id, version, ordinal);
		this.item = new ItemDTO(itemId, itemValue, null, null, itemCategory);
		this.groupName = groupName;
		this.description = description;
		this.remarks = remarks;
		this.tableView = tableView;
		
	}
	
	
	/**
	 * @param processItem
	 */
	public ProcessItemDTO(ProcessItem processItem) {
		super(processItem.getId(), processItem.getVersion(), processItem.getOrdinal());
		this.item = new ItemDTO(processItem.getItem());
		
		this.groupName = processItem.getGroupName();
		this.description = processItem.getDescription();
		this.remarks = processItem.getRemarks();
		this.tableView = processItem.isTableView();
		
		setStorageForms(Arrays.stream(processItem.getStorageForms())
				.map(i->{return new StorageBaseDTO(i);})
				.collect(Collectors.toList()));

		
	}

	public ProcessItemDTO(Integer id, Integer version, Integer ordinal,
			ItemDTO item,
			String groupName, String description, String remarks) {
		super(id, version, ordinal);
		this.item = item;
		this.groupName = groupName;
		this.description = description;
		this.remarks = remarks;
	}
	
	public void setStorageForms(Collection<StorageBaseDTO> storageForms) {
		this.storageForms = new TreeSet<>(Ordinal.ordinalComparator());
		this.storageForms.addAll(storageForms);
		this.totalAmount = new AmountWithUnit[2];
		AmountWithUnit totalAmount = this.storageForms.stream()
				.map(sf -> sf.getUnitAmount()
						.substract(Optional.ofNullable(sf.getContainerWeight()).orElse(BigDecimal.ZERO))
						.multiply(sf.getNumberUnits()))
				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
		this.totalAmount[0] = totalAmount.setScale(MeasureUnit.SCALE);
		this.totalAmount[1] = totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE);
	}
	
	public Set<StorageBaseDTO> getStorageForms() {
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
				if(warehouse != null)
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
		
}
