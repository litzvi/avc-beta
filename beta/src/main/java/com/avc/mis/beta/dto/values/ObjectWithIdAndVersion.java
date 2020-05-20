/**
 * 
 */
package com.avc.mis.beta.dto.values;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.process.OrderItem;

/**
 * @author Zvi
 *
 */
public class ObjectWithIdAndVersion extends DataDTO {

	public ObjectWithIdAndVersion(Integer id, Integer version) {
		super(id, version);
	}

	/**
	 * @param orderItem
	 */
	public ObjectWithIdAndVersion(OrderItem orderItem) {
		super(orderItem.getId(), orderItem.getVersion());
	}

}
