/**
 * 
 */
package com.avc.mis.beta.entities;

/**
 * @author Zvi
 *
 * Interface for entities that can be soft deleted - set as not active but not be fiscally removed.
 */
public interface SoftDeleted extends Insertable {

	public void setActive(boolean active);	
}
