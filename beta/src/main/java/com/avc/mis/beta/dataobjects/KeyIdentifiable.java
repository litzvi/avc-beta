/**
 * 
 */
package com.avc.mis.beta.dataobjects;

/**
 * @author Zvi
 *
 */
public interface KeyIdentifiable {
	
	public Integer getId();	
	
	static <T extends KeyIdentifiable> boolean canEqualCheckNullId(T t, Object o) {
		if(t.getClass().isInstance(o)) {
			KeyIdentifiable other = (KeyIdentifiable) o;
			return !(t.getId() == null && other.getId() == null);
		}
		return false;
		
	}
}
