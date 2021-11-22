/**
 * 
 */
package com.avc.mis.beta.dto.basic;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class BasicDataEntity<T extends DataEntity> extends DataDTO {
	
	public BasicDataEntity(Integer id, Integer version) {
		super(id, version);
	}
	
	/**
	 * @param entity
	 */
	public BasicDataEntity(@NonNull T entity) {
		super(entity);
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return DataEntity.class;
	}
}
