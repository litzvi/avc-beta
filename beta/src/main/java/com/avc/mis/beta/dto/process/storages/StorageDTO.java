/**
 * 
 */
package com.avc.mis.beta.dto.process.storages;

import java.math.BigDecimal;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.process.storages.Storage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Used for representing inventory storage.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class StorageDTO extends StorageBaseDTO {
	
		
	public StorageDTO(Integer id, Integer version, Integer ordinal,
			BigDecimal unitAmount, BigDecimal numberUnits, //BigDecimal accessWeight,
			Integer warehouseLocationId,  String warehouseLocationValue,
			String remarks, Class<? extends Storage> clazz) {
		super(id, version, ordinal, unitAmount, numberUnits, warehouseLocationId, warehouseLocationValue, remarks, clazz);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Storage.class;
	}
	
	@Override
	public Storage fillEntity(Object entity) {
		Storage storage;
		if(entity instanceof Storage) {
			storage = (Storage) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Storage class");
		}
		super.fillEntity(storage);
		
		return storage;
	}

	
	
}
