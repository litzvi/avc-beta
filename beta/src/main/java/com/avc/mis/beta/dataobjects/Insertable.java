/**
 * 
 */
package com.avc.mis.beta.dataobjects;

/**
 * @author Zvi
 *
 */
public interface Insertable {
	
	public boolean isLegal();	
	public Integer getId();
//	public String getValue();
	
	static <T extends Insertable> boolean canEqualCheckNullId(T t, Object o) {
		if(t.getClass().isInstance(o)) {
			Insertable other = (Insertable) o;
			return !(t.getId() == null && other.getId() == null);
		}
		return false;
		
	}

}
