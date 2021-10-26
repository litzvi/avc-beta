/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.data.ProcessFile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for entities that can be edited by multiple users.
 * contains a version.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class DataDTO extends BaseEntityDTO {
	
	@EqualsAndHashCode.Exclude
	private Integer version;
	
	public DataDTO(Integer id, Integer version) {
		super(id);
		this.version = version;
	}
	
	public DataDTO(DataEntity entity) {
		super(entity);
		this.version = entity.getVersion();
	}
	
	@Override
	public DataEntity fillEntity(Object entity) {
		DataEntity dataEntity;
		if(entity instanceof DataEntity) {
			dataEntity = (DataEntity) entity;
		}
		else {
			throw new IllegalArgumentException("Param has to be DataEntity class");
		}
		super.fillEntity(dataEntity);
		dataEntity.setVersion(getVersion());
		return dataEntity;
	}
}
