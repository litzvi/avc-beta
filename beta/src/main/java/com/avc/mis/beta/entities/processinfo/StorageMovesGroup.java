/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.avc.mis.beta.dto.process.inventory.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.process.inventory.MovedItemTableDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.process.inventory.Storage;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.process.inventory.UsedItemBase;
import com.avc.mis.beta.entities.values.Warehouse;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Groups storage moves of a relocation process, 
 * in order to connect between them for display purposes.
 * 
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_MOVES_GROUP")
@PrimaryKeyJoinColumn(name = "groupId")
public class StorageMovesGroup extends ProcessGroupWithStorages {
	
	{
		setDtype("StorageMovesGroup");
	}
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "group", targetEntity = UsedItemBase.class, orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//	@NotEmpty(message = "Has to containe at least one storage move")
	private Set<StorageMove> storageMoves = new HashSet<>();
	
	/**
	 * Gets the list of storage moves as an array (can be ordered).
	 * @return the storageMoves
	 */
	public StorageMove[] getStorageMoves() {
		StorageMove[] storageMoves = this.storageMoves.toArray(new StorageMove[this.storageMoves.size()]);
		Arrays.sort(storageMoves, Ordinal.ordinalComparator());
		return storageMoves;
	}

	/**
	 * Setter for adding storage moves, 
	 * receives an array (which can be ordered, for later use to add an order to the storage moves).
	 * Filters the not legal moves and set needed references to satisfy needed foreign keys of database.
	 * @param storageMoves the storageMoves to set
	 */
	public void setStorageMoves(StorageMove[] storageMoves) {
		Ordinal.setOrdinals(storageMoves);
		this.storageMoves = Insertable.setReferences(storageMoves, (t) -> {t.setReference(this);	return t;});
	}
	
	public void setStorageMove(MovedItemTableDTO movedItemTable) {
		setTableView(true);
		
		//same as the original process item, for convenience in queries
		setMeasureUnit(movedItemTable.getMeasureUnit());
		BigDecimal accessWeight = movedItemTable.getAccessWeight();
		Warehouse warehouse = movedItemTable.getNewWarehouseLocation();
		List<BasicUsedStorageDTO> basicUsedStorages = movedItemTable.getAmounts();
		StorageMove[] storageMoves = new StorageMove[basicUsedStorages.size()];
		for(int i=0; i<storageMoves.length; i++) {
			BasicUsedStorageDTO basicUsedStorage = basicUsedStorages.get(i);
			storageMoves[i] = new StorageMove();
			storageMoves[i].setId(basicUsedStorage.getId());
			storageMoves[i].setVersion(basicUsedStorage.getVersion());
			storageMoves[i].setNumberUsedUnits(basicUsedStorage.getAmount());
			Storage storage = new Storage();
			storage.setId(basicUsedStorage.getStorageId());
			storage.setVersion(basicUsedStorage.getStorageVersion());
			storageMoves[i].setStorage(storage);
			
			storageMoves[i].setOrdinal(basicUsedStorage.getOrdinal());
			storageMoves[i].setNumberUnits(basicUsedStorage.getAmount());
			storageMoves[i].setAccessWeight(accessWeight);
			storageMoves[i].setWarehouseLocation(warehouse);

		}
		setStorageMoves(storageMoves);	
		
	}


}
