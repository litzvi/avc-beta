/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.DataInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DataObject<T extends DataInterface>  extends DataDTO {

	public DataObject(Integer id, Integer version) {
		super(id, version);
	}

	/**
	 * @param entity
	 */
	public DataObject(T entity) {
		super(entity.getId(), entity.getVersion());
	}
	
	@JsonIgnore
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
		
		return dataEntity;
	}

}
