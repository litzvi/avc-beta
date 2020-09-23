/**
 * 
 */
package com.avc.mis.beta.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	protected boolean canEqual(Object o) {
		if(this.getClass().isInstance(o)) {
			EntityId other = (EntityId) o;
			return !(this.getId() == null && other.getId() == null);
		}
		return false;
	}
}
