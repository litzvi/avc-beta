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
	public StorageRelocation fillEntity(Object entity) {
		StorageRelocation relocation;
		if(entity instanceof StorageRelocation) {
			relocation = (StorageRelocation) entity;
		}
		else {
			throw new IllegalStateException("Param has to be StorageRelocation class");
		}
		super.fillEntity(relocation);
		
		if(getPoCode() == null) {
			throw new IllegalArgumentException("Storage relocation has to reference a po code");
		}
		
		return relocation;
	}

	
	@Override
	public String getProcessTypeDescription() {
		return "Storage relocation";
	}

}
