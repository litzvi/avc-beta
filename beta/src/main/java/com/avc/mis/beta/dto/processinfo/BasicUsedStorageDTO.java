/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.processinfo.StorageBase;
import com.avc.mis.beta.entities.processinfo.UsedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicUsedStorageDTO extends ProcessDTO {
	
	private Integer storageId;
	private Integer storageVersion;
	private Integer ordinal; //the storage ordinal
	private BigDecimal amount;
	
//	private StorageDTO storage;
	
//	public BasicUsedStorageDTO(Integer id, Integer version, StorageDTO storage) {
//		super(id, version);
//		this.storage = storage;
//	}
	
	public BasicUsedStorageDTO(Integer id, Integer version, 
			Integer storageId, Integer storageVersion, Integer ordinal, BigDecimal amount) {
		super(id, version);
		this.storageId = storageId;
		this.storageVersion = storageVersion;
		this.ordinal = ordinal;
		this.amount = amount;
			
	}

	public BasicUsedStorageDTO(@NonNull UsedItem usedItem) {
		super(usedItem.getId(), usedItem.getVersion());
		StorageBase storage = usedItem.getStorage();
		this.storageId = storage.getId();
		this.storageVersion = storage.getVersion();
		this.ordinal = storage.getOrdinal();
		this.amount = storage.getUnitAmount().getAmount();
	}

}
