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

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.MovedItemTableDTO;
import com.avc.mis.beta.dto.process.inventory.StorageDTO;
import com.avc.mis.beta.dto.process.inventory.StorageMoveDTO;
import com.avc.mis.beta.dto.query.StorageMoveWithGroup;
import com.avc.mis.beta.dto.values.BasicValueEntity;
import com.avc.mis.beta.dto.values.DataObject;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.processinfo.StorageMovesGroup;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageMovesGroupDTO extends ProcessGroupDTO {

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
			this.storageMoves.stream().findAny().ifPresent(s -> {
				movedItemTable.setItem(s.getItem());
				movedItemTable.setMeasureUnit(s.getMeasureUnit());
				movedItemTable.setItemPo(s.getItemPo());
				BasicValueEntity<Warehouse> warehouse = s.getWarehouseLocation();
				if(warehouse != null)
					movedItemTable.setNewWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
//				StorageDTO storage = s.getStorage();
				movedItemTable.setContainerWeight(s.getStorageContainerWeight());
				warehouse = s.getStorageWarehouseLocation();
				if(warehouse != null)
					movedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				
			});
			
			List<BasicUsedStorageDTO> used = this.storageMoves.stream().map((s) -> {
				StorageDTO storage = s.getStorage();
				return new BasicUsedStorageDTO(s.getId(), s.getVersion(), 
						storage.getId(), storage.getVersion(), s.getStorageOrdinal(), s.getStorageNumberUnits());
			}).collect(Collectors.toList());
			movedItemTable.setAmounts(used);
			return movedItemTable;
		}
		return null;
	}
	
	public AmountWithUnit[] getTotalAmount() {
		BigDecimal total = this.storageMoves.stream()
				.map(ui -> ui.getTotal())
				.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		
//		BigDecimal total = this.storageMoves.stream()
//				.map(ui -> ui.getStorage().getUnitAmount()
//						.substract(Optional.ofNullable(ui.getStorage().getContainerWeight()).orElse(BigDecimal.ZERO))
//						.multiply(ui.getNumberUsedUnits()))
//				.reduce(AmountWithUnit::add).orElse(AmountWithUnit.ZERO_KG);
//		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
		return new AmountWithUnit[] {totalAmount.setScale(MeasureUnit.SCALE),
				totalAmount.convert(MeasureUnit.LOT).setScale(MeasureUnit.SCALE)};
	}
	
	public static List<StorageMovesGroupDTO> getStorageMoveGroups(List<StorageMoveWithGroup> storageMoves) {
		Map<Integer, List<StorageMoveWithGroup>> map = storageMoves.stream()
				.collect(Collectors.groupingBy(StorageMoveWithGroup::getId, LinkedHashMap::new, Collectors.toList()));
		List<StorageMovesGroupDTO> storageMovesGroups = new ArrayList<>();
		for(List<StorageMoveWithGroup> list: map.values()) {
			StorageMovesGroupDTO storageMovesGroup = list.get(0).getStorageMovesGroup();
			storageMovesGroup.setStorageMoves(list.stream()
					.map(i -> i.getStorageMove())
//					.sorted(Ordinal.ordinalComparator())
					.collect(Collectors.toList()));
			storageMovesGroups.add(storageMovesGroup);
		}
//		usedItemsGroups.sort(Ordinal.ordinalComparator());
		return storageMovesGroups;
	}
}
