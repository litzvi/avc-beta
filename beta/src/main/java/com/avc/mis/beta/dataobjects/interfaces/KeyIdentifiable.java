/**
 * 
 */
package com.avc.mis.beta.dataobjects.interfaces;

/**
 * @author Zvi
 *
 */
public interface KeyIdentifiable {
	
	public Integer getId();	
//	private boolean canEqual(Object o);
	
	static <T extends KeyIdentifiable> boolean canEqualCheckNullId(T t, Object o) {		
		return canEqualCheckNullId(t, t.getClass(), o);
		
	}
	
	static <T extends KeyIdentifiable> boolean canEqualCheckNullId(T t, Class<?> tClass, Object o) {
		if(tClass.isInstance(o)) {
			KeyIdentifiable other = (KeyIdentifiable) o;
			return !(t.getId() == null && other.getId() == null);
		}
		return false;
		
	}
}
