/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.MovedItemTableDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageMoveDTO;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;
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
public class StorageMovesGroupDTO extends ProcessGroupDTO implements ListGroup<StorageMoveDTO> {

	@JsonIgnore
	private MeasureUnit measureUnit;
	private List<StorageMoveDTO> storageMoves;


	public StorageMovesGroupDTO(Integer id, Integer version, Integer ordinal,
			MeasureUnit measureUnit, String groupName, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
		this.measureUnit = measureUnit;
	}	

	public StorageMovesGroupDTO(StorageMovesGroup group) {
		super(group);
		this.measureUnit = group.getMeasureUnit();
		this.storageMoves = (Arrays.stream(group.getStorageMoves())
				.map(u->{return new StorageMoveDTO(u);})
				.collect(Collectors.toList()));
	}
	
	public List<StorageMoveDTO> getStorageMoves() {
		if(isTableView()) {
			return null;
		}
		return this.storageMoves;
	}
	
	public MovedItemTableDTO getStorageMove() {
		if(isTableView() && this.storageMoves != null && !this.storageMoves.isEmpty()) {
			MovedItemTableDTO movedItemTable = new MovedItemTableDTO();
			this.storageMoves.stream().findAny().ifPresent(m -> {
				movedItemTable.setItem(m.getItem());
				movedItemTable.setMeasureUnit(m.getMeasureUnit());
				movedItemTable.setItemPo(m.getItemPo());
				BasicValueEntity<Warehouse> warehouse = m.getWarehouseLocation();
				if(warehouse != null)
					movedItemTable.setNewWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				StorageDTO storage = m.getStorage();
				movedItemTable.setAccessWeight(storage.getAccessWeight());
				warehouse = storage.getWarehouseLocation();
				if(warehouse != null)
					movedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				
			});
			
			List<BasicUsedStorageDTO> used = this.storageMoves.stream().map((m) -> {
				StorageDTO storage = m.getStorage();
				return new BasicUsedStorageDTO(m.getId(), m.getVersion(), 
						storage.getId(), storage.getVersion(), storage.getOrdinal(), storage.getNumberUnits());
			}).collect(Collectors.toList());
			movedItemTable.setAmounts(used);
			return movedItemTable;
		}
		return null;
	}
	
	public AmountWithUnit getTotalAmount() {
		BigDecimal total = this.storageMoves.stream()
				.map(m -> m.getTotal())
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		return totalAmount;
//		return new AmountWithUnit[] {totalAmount.setScale(MeasureUnit.SCALE),
//				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}
	
	/**
	 * static function for building List of StorageMovesGroupDTO from a List of StorageMoveWithGroup
	 * received by a join query of storageMoves with their group.
	 * @param storageMovesWithGroup a List<StorageMoveWithGroup>
	 * @return List<StorageMovesGroupDTO> as in the DTO structure.
	 */
//	public static List<StorageMovesGroupDTO> getStorageMoveGroups(List<StorageMoveWithGroup> storageMovesWithGroup) {
//		Map<Integer, List<StorageMoveWithGroup>> map = storageMovesWithGroup.stream()
//				.collect(Collectors.groupingBy(StorageMoveWithGroup::getId, LinkedHashMap::new, Collectors.toList()));
//		List<StorageMovesGroupDTO> storageMovesGroups = new ArrayList<>();
//		for(List<StorageMoveWithGroup> list: map.values()) {
//			StorageMovesGroupDTO storageMovesGroup = list.get(0).getStorageMovesGroup();
//			storageMovesGroup.setStorageMoves(list.stream()
//					.map(i -> i.getStorageMove())
////					.sorted(Ordinal.ordinalComparator())
//					.collect(Collectors.toList()));
//			storageMovesGroups.add(storageMovesGroup);
//		}
////		usedItemsGroups.sort(Ordinal.ordinalComparator());
//		return storageMovesGroups;
//	}

	@JsonIgnore
	@Override
	public void setList(List<StorageMoveDTO> list) {
		setStorageMoves(list);
	}
}
