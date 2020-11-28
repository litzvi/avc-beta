/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.DataEntity;

import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DataObject<T extends DataEntity>  extends DataDTO {

	public DataObject(Integer id, Integer version) {
		super(id, version);
	}

	/**
	 * @param entity
	 */
	public DataObject(T entity) {
		super(entity.getId(), entity.getVersion());
	}

}
