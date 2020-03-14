/**
 * 
 */
package com.avc.mis.beta.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author Zvi
 *
 */
public interface Insertable extends Serializable{
	
	public Integer getId();
	
	/**
	 * Checks if object has the required not null-able data.
	 * Dosen't check if required references - foreign keys for database are set.
	 * @return true if all required data is set, false otherwise.
	 */
	public boolean isLegal();
	public void prePersistOrUpdate();

	/**
	 * Empty implementation
	 */
	default public void setReference(Object referenced) {}

//	public String getValue();
	
	static <T extends Insertable> Set<T> filterAndSetReference(T[] tArray, UnaryOperator<T> p) {
		return Arrays.stream(tArray)
			.filter(t -> t.isLegal())
			.map(t -> p.apply(t))
			.collect(Collectors.toSet());
	}
	
	static <T extends Insertable> boolean canEqualCheckNullId(T t, Object o) {		
		if(t.getClass().isInstance(o)) {
			Insertable other = (Insertable) o;
			return !(t.getId() == null && other.getId() == null);
		}
		return false;
		
	}
	
}
