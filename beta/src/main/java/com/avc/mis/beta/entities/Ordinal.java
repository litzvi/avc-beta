/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Comparator;

/**
 *	Interface for object that have an Integer as an ordinal value.
 *
 * @author Zvi
 *
 */
public interface Ordinal {
	public Integer getOrdinal();
	public void setOrdinal(Integer ordinal);
	

	/**
	 * Compares two objects by their ordinal value, smaller values are first in order.
	 * @param <T>
	 * @return comparator with a method for comparing by ordinal value. 
	 */
	public static <T extends Ordinal> Comparator<T> ordinalComparator() {
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return o1.getOrdinal()-o2.getOrdinal();
			}};
		
	}
	
	public static void setOrdinals(Ordinal[] array) {
		for(int i=0; i<array.length; i++) {
			array[i].setOrdinal(i);
		}
	}
}
