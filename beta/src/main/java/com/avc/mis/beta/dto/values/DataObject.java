/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.DataEntity;

import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DataObject extends DataDTO {

	public DataObject(Integer id, Integer version) {
		super(id, version);
	}

	/**
	 * @param entity
	 */
	public DataObject(DataEntity entity) {
		super(entity.getId(), entity.getVersion());
	}

}
