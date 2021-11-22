/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.BasicDataDTO;
import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.DataInterface;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DataObject<T extends DataInterface>  extends BasicDataDTO {

	public DataObject(Integer id, Integer version) {
		super(id, version);
	}

	/**
	 * @param entity
	 */
	public DataObject(T entity) {
		super(entity.getId(), entity.getVersion());
	}
	
	@Override
	public DataEntity fillEntity(Object entity) {
		DataEntity dataEntity;
		if(entity instanceof DataEntity) {
			dataEntity = (DataEntity) entity;
		}
		else {
			throw new IllegalStateException("Param has to be DataEntity class");
		}
		super.fillEntity(dataEntity);
		
		return dataEntity;
	}
	

}
