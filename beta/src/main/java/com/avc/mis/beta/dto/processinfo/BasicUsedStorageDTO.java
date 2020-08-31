/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

import com.avc.mis.beta.dto.ProcessDTO;
import com.avc.mis.beta.entities.processinfo.Storage;
import com.avc.mis.beta.entities.processinfo.UsedItem;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BasicUsedStorageDTO extends ProcessDTO {
	
//	private Integer ordinal;
//	private BigDecimal amount;
	
	private StorageDTO storage;
	
	public BasicUsedStorageDTO(Integer id, Integer version, StorageDTO storage) {
		super(id, version);
		this.storage = storage;
	}

	public BasicUsedStorageDTO(@NonNull UsedItem usedItem) {
		super(usedItem.getId(), usedItem.getVersion());
		this.storage = new StorageDTO(usedItem.getStorage());
	}

}
