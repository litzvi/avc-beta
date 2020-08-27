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
	

	private Integer ordinal;
	private BigDecimal amount;
	
	private Storage storage;
	
	public BasicUsedStorageDTO(Integer id, Integer version, 
			Integer ordinal, BigDecimal amount) {
		super(id, version);
		this.ordinal = ordinal;
		this.amount = amount;
	}
	
//	public BasicUsedStorageDTO(Integer id, Integer version, Integer storageId, Integer storageVersion) {
//		super(id, version);
//		this.storage = new BasicStorageDTO(storageId, storageVersion);
//	}

	public BasicUsedStorageDTO(@NonNull UsedItem usedItem) {
		super(usedItem.getId(), usedItem.getVersion());
		this.storage = usedItem.getStorage();
	}

	
}
