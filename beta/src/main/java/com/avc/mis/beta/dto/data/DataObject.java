/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.DataInterface;

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

}
