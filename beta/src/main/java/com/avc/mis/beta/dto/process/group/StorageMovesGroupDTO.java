/**
 * 
 */
package com.avc.mis.beta.dto.process.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.process.storages.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.storages.MovedItemTableDTO;
import com.avc.mis.beta.dto.process.storages.StorageBaseDTO;
import com.avc.mis.beta.dto.process.storages.StorageMoveDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.process.group.StorageMovesGroup;
import com.avc.mis.beta.entities.process.storages.StorageMove;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO that contains a group of storage moves. 
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StorageMovesGroupDTO extends ProcessGroupDTO {

	private List<StorageMoveDTO> storageMoves;


	public StorageMovesGroupDTO(Integer id, Integer version, Integer ordinal,
			String groupName, boolean tableView) {
		super(id, version, ordinal, groupName, tableView);
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
			storageMove.setWarehouseLocation(movedItemTable.getNewWarehouseLocation());

		}
		setStorageMoves(storageMoves);	
		
	}
	
	public List<StorageMoveDTO> getStorageMovesField() {
		return this.storageMoves;
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
				movedItemTable.setNewWarehouseLocation(m.getWarehouseLocation());
				movedItemTable.setItemPoCodes(m.getItemPoCodes());
				movedItemTable.setItemSuppliers(m.getItemSuppliers());
				StorageBaseDTO storage = m.getStorage();
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
		if(getStorageMovesField() == null || getStorageMovesField().isEmpty()) {
			throw new IllegalArgumentException("Has to containe at least one storage move");
		}
		else {
			Ordinal.setOrdinals(getStorageMovesField());
			group.setStorageMoves(getStorageMovesField().stream().map(i -> i.fillEntity(new StorageMove())).collect(Collectors.toSet()));
		}
		
		return group;
	}
}
