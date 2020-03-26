/**
 * 
 */
package com.avc.mis.beta.entities;

/**
 * @author Zvi
 *
 */
public interface SoftDeleted extends Insertable{

	/**
	 * @param b
	 */
	public void setActive(boolean b);
	
}
