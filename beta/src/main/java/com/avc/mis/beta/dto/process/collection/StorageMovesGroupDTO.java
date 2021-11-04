/**
 * 
 */
package com.avc.mis.beta.dto.process.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.MovedItemTableDTO;
import com.avc.mis.beta.dto.process.inventory.StorageBaseDTO;
import com.avc.mis.beta.dto.process.inventory.StorageMoveDTO;
import com.avc.mis.beta.dto.reference.BasicValueEntity;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.RelocationProcess;
import com.avc.mis.beta.entities.process.collection.ApprovalTask;
import com.avc.mis.beta.entities.process.collection.StorageMovesGroup;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.values.Warehouse;
import com.avc.mis.beta.utilities.ListGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StorageMovesGroupDTO extends ProcessGroupDTO implements ListGroup<StorageMoveDTO> {

//	@JsonIgnore
//	private MeasureUnit measureUnit;
	private List<StorageMoveDTO> storageMoves;


	public StorageMovesGroupDTO(Integer id, Integer version, Integer ordinal,
			String groupName, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
//		this.measureUnit = measureUnit;
	}	

	public StorageMovesGroupDTO(StorageMovesGroup group) {
		super(group);
//		this.measureUnit = group.getMeasureUnit();
		this.storageMoves = (group.getStorageMoves().stream()
				.map(u->{return new StorageMoveDTO(u);})
				.collect(Collectors.toList()));
	}
	
	public void setStorageMove(MovedItemTableDTO movedItemTable) {
		setTableView(true);
		
		List<StorageMoveDTO> storageMoves = new ArrayList<StorageMoveDTO>();
		for(BasicUsedStorageDTO basicUsedStorage: movedItemTable.getAmounts()) {
			StorageMoveDTO storageMove = new StorageMoveDTO();
			storageMoves.add(storageMove);
			storageMove.setId(basicUsedStorage.getId());
			storageMove.setVersion(basicUsedStorage.getVersion());
			storageMove.setNumberUsedUnits(basicUsedStorage.getAmount());
			StorageBaseDTO storage = new StorageBaseDTO();
			storage.setId(basicUsedStorage.getStorageId());
			storage.setVersion(basicUsedStorage.getStorageVersion());
			storageMove.setStorage(storage);
			
			storageMove.setOrdinal(basicUsedStorage.getOrdinal());
//			storageMove.setNumberUnits(basicUsedStorage.getAmount());
//			storageMove.setAccessWeight(accessWeight);
			storageMove.setWarehouseLocation(movedItemTable.getNewWarehouseLocation());

		}
		setStorageMoves(storageMoves);	
		
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
				movedItemTable.setItemProcessDate(m.getItemProcessDate());
//				BasicValueEntity<Warehouse> warehouse = m.getWarehouseLocation();
//				if(warehouse != null)
//					movedItemTable.setNewWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				movedItemTable.setNewWarehouseLocation(m.getWarehouseLocation());
				movedItemTable.setItemPoCodes(m.getItemPoCodes());
				movedItemTable.setItemSuppliers(m.getItemSuppliers());
				StorageBaseDTO storage = m.getStorage();
//				movedItemTable.setAccessWeight(storage.getAccessWeight());
//				warehouse = storage.getWarehouseLocation();
//				if(warehouse != null)
//					movedItemTable.setWarehouseLocation(new Warehouse(warehouse.getId(), warehouse.getValue()));
				movedItemTable.setWarehouseLocation(storage.getWarehouseLocation());
				
			});
			
			List<BasicUsedStorageDTO> used = this.storageMoves.stream().map((m) -> {
				StorageBaseDTO storage = m.getStorage();
				return new BasicUsedStorageDTO(m.getId(), m.getVersion(), 
						storage.getId(), storage.getVersion(), storage.getOrdinal(), storage.getNumberUnits(), 
						m.getNumberUsedUnits(), m.getNumberAvailableUnits());
			}).collect(Collectors.toList());
			movedItemTable.setAmounts(used);
			return movedItemTable;
		}
		return null;
	}
	
	public List<AmountWithUnit> getTotalAmount() {
		if(storageMoves == null || storageMoves.isEmpty()) {
			return null;
		}
		AmountWithUnit totalAmount;
		try {
			totalAmount = storageMoves.stream()
				.map(ui -> {
					if(MeasureUnit.NONE == ui.getItem().getUnit().getMeasureUnit()) {
						return new AmountWithUnit(ui.getTotal(), ui.getMeasureUnit());
					}
					else {
						return ui.getItem().getUnit().multiply(ui.getTotal());
					}
				})
				.reduce(AmountWithUnit::add).get();
		} catch (NoSuchElementException | UnsupportedOperationException e) {
			return null;
		}
		return Arrays.asList(totalAmount.setScale(MeasureUnit.SUM_DISPLAY_SCALE));
	}
	
//	public List<AmountWithUnit> getTotalAmount() {
//		
//		AmountWithUnit totalAmount = this.storageMoves.stream()
//				.map(m -> m.getTotal())
//				.reduce(AmountWithUnit::add).orElse(null);
////		AmountWithUnit totalAmount = new AmountWithUnit(total, this.measureUnit);
//		if(totalAmount == null) {
//			return null;
//		}
////		return AmountWithUnit.weightDisplay(totalAmount, Arrays.asList(MeasureUnit.KG, MeasureUnit.LBS));
//		return Arrays.asList(totalAmount.setScale(MeasureUnit.SUM_DISPLAY_SCALE));
//	}
	
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
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return StorageMovesGroup.class;
	}
	
	@Override
	public StorageMovesGroup fillEntity(Object entity) {
		StorageMovesGroup group;
		if(entity instanceof StorageMovesGroup) {
			group = (StorageMovesGroup) entity;
		}
		else {
			throw new IllegalStateException("Param has to be StorageMovesGroup class");
		}
		super.fillEntity(group);
		if(getStorageMoves() == null || getStorageMoves().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one storage move");
		}
		else {
			Ordinal.setOrdinals(getStorageMoves());
			group.setStorageMoves(getStorageMoves().stream().map(i -> i.fillEntity(new StorageMove())).collect(Collectors.toSet()));
		}
		
		return group;
	}
}
