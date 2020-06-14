/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;

/**
 * Interface for entities that can be soft deleted - set as not active but not be fiscally removed.
 * 
 * @author Zvi
 *
 */
@MappedSuperclass
public interface SoftDeleted extends Insertable {

	public void setActive(boolean active);	
}
