/**
 * 
 */
package com.avc.mis.beta.entities.process;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.avc.mis.beta.dto.processinfo.BasicStorageDTO;
import com.avc.mis.beta.dto.processinfo.BasicUsedStorageDTO;
import com.avc.mis.beta.dto.processinfo.UsedItemTableDTO;
import com.avc.mis.beta.entities.Insertable;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;
import com.avc.mis.beta.entities.enums.MeasureUnit;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.StorageMove;
import com.avc.mis.beta.entities.processinfo.UsedItem;
import com.avc.mis.beta.entities.values.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "STORAGE_RELOCATIONS")
@PrimaryKeyJoinColumn(name = "processId")
public class StorageRelocation extends PoProcess {
	
	@Setter(value = AccessLevel.NONE) @Getter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "process", orphanRemoval = true, 
		cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<StorageMove> storageMoves = new HashSet<>();
	
	@Setter(value = AccessLevel.NONE) 
	@JsonIgnore
	@Column(nullable = false)
	private boolean tableView = false;

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
	
	public void setStorageMove(UsedItemTableDTO usedItemTable) {
		this.tableView = true;
		
		MeasureUnit measureUnit = usedItemTable.getMeasureUnit();
		BigDecimal containerWeight = usedItemTable.getContainerWeight();
		Warehouse warehouse = usedItemTable.getWarehouseLocation();
		
		List<BasicUsedStorageDTO> basicUsedStorages = usedItemTable.getAmounts();
		StorageMove[] storageMoves = new StorageMove[basicUsedStorages.size()];
		for(int i=0; i<storageMoves.length; i++) {
			BasicUsedStorageDTO basicUsedStorage = basicUsedStorages.get(i);
			storageMoves[i] = new StorageMove();
			storageMoves[i].setId(basicUsedStorage.getId());
			storageMoves[i].setVersion(basicUsedStorage.getVersion());
			Storage storage = new Storage();
			storage.setId(basicUsedStorage.getStorageId());
			storage.setVersion(basicUsedStorage.getStorageVersion());
			storageMoves[i].setStorage(storage);
			
			storageMoves[i].setOrdinal(basicUsedStorage.getOrdinal());
			storageMoves[i].setUnitAmount(new AmountWithUnit(basicUsedStorage.getAmount(), measureUnit));
			storageMoves[i].setContainerWeight(containerWeight);
			storageMoves[i].setWarehouseLocation(warehouse);

		}
		setStorageMoves(storageMoves);	
		
	}

	@Override
	public String getProcessTypeDescription() {
		return "Storage Relocation";
	}

}
