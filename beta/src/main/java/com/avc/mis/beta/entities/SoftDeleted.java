/**
 * 
 */
package com.avc.mis.beta.entities;

/**
 * Interface for entities that can be soft deleted - set as not active but not be fiscally removed.
 * 
 * @author Zvi
 *
 */
public interface SoftDeleted extends Insertable {

	public void setActive(boolean active);	
}
