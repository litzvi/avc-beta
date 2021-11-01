/**
 * 
 */
package com.avc.mis.beta.dto.process;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.StorageRelocation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class StorageRelocationDTO extends RelocationProcessDTO {
	
	public StorageRelocationDTO(@NonNull StorageRelocation relocation) {
		super(relocation);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return StorageRelocation.class;
	}
	
	@Override
	public String getProcessTypeDescription() {
		return "Storage relocation";
	}

}
