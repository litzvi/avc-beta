/**
 * 
 */
package com.avc.mis.beta.dto.processinfo;

import java.math.BigDecimal;

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
public class BasicUsedStorageDTO extends BasicStorageDTO {
	
	public BasicUsedStorageDTO(Integer id, Integer version, Integer ordinal, BigDecimal amount) {
		super(id, version, ordinal, amount);
	}

	public BasicUsedStorageDTO(@NonNull UsedItem usedItem) {
		super(usedItem.getStorage());
	}

	
}
