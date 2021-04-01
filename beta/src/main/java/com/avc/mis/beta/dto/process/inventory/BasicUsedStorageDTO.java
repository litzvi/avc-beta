/**
 * 
 */
package com.avc.mis.beta.dto.process.inventory;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.process.inventory.StorageBase;
import com.avc.mis.beta.entities.process.inventory.StorageMove;
import com.avc.mis.beta.entities.process.inventory.UsedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

/**
 * @author zvi
 *
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BasicUsedStorageDTO extends SubjectDataDTO {
	
	Integer storageId;
	Integer storageVersion;
//	Integer ordinal; //the storage ordinal
	BigDecimal storageAmount;
	
	BigDecimal numberUsedUnits;
	BigDecimal numberAvailableUnits;
	
//	StorageDTO storage;
	
//	public BasicUsedStorageDTO(Integer id, Integer version, StorageDTO storage) {
//		super(id, version);
//		this.storage = storage;
//	}
	
	/**
	 * Used for UsedItemTable
	 */
	public BasicUsedStorageDTO(Integer id, Integer version, 
			Integer storageId, Integer storageVersion, Integer ordinal, BigDecimal storageAmount, 
			BigDecimal numberUsedUnits, BigDecimal numberAvailableUnits) {
		super(id, version, ordinal);
		this.storageId = storageId;
		this.storageVersion = storageVersion;
//		this.ordinal = ordinal;
		this.storageAmount = storageAmount;
		this.numberUsedUnits = numberUsedUnits;
		this.numberAvailableUnits = numberAvailableUnits;
	}
	
	/**
	 * Used for MovedItemTable
	 */
//	public BasicUsedStorageDTO(Integer id, Integer version, 
//			Integer storageId, Integer storageVersion, Integer ordinal, BigDecimal storageAmount) {
//		super(id, version, ordinal);
//		this.storageId = storageId;
//		this.storageVersion = storageVersion;
////		this.ordinal = ordinal;
//		this.storageAmount = storageAmount;
//	}
	
	

//	public BasicUsedStorageDTO(@NonNull UsedItem usedItem) {
//		super(usedItem.getId(), usedItem.getVersion());
//		StorageBase storage = usedItem.getStorage();
//		this.storageId = storage.getId();
//		this.storageVersion = storage.getVersion();
//		setOrdinal(storage.getOrdinal());
//		this.storageAmount = storage.getNumberUnits();
//	}
//	
//	public BasicUsedStorageDTO(@NonNull StorageMove storageMove) {
//		super(storageMove.getId(), storageMove.getVersion());
//		StorageBase storage = storageMove.getStorage();
//		this.storageId = storage.getId();
//		this.storageVersion = storage.getVersion();
//		setOrdinal(storageMove.getOrdinal());
//		this.storageAmount = storageMove.getNumberUnits();
//	}

}
