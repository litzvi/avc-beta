/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Interface for object that have an Integer as an ordinal value,
 * which implies an order between a collection of objects. 
 *
 * @author Zvi
 *
 */
public interface Ordinal {
	
	public Integer getOrdinal();
	public void setOrdinal(Integer ordinal);
	
	public Integer getId();
	

	/**
	 * Compares two objects by their ordinal value, smaller values are first in order.
	 * If the ordinal value is equal and objects are of different classes,
	 * compares by lexicographic order of simple class name.
	 * @param <T> a class that implements Ordinal interface.
	 * @return comparator with a method for comparing by ordinal value. 
	 */
	public static <T extends Ordinal> Comparator<T> ordinalComparator() {
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				int result = o1.getOrdinal()-o2.getOrdinal();
				if(result == 0) {
					result = o1.getClass().getSimpleName().compareTo(o2.getClass().getSimpleName());
//					if(result == 0) {
//						result = Integer.compare(o1.getId(), o2.getId());
//					}
				}
				
				return result;
			}};
		
	}
	
	/**
	 * Helper function for setting default ordinal values, when not set.
	 * If one all the ordinal values are null, will set according to received order.
	 * @param array of objects with ordinal field to set.
	 */
	public static void setOrdinals(Ordinal[] array) {
		if(Arrays.stream(array).allMatch(o -> o.getOrdinal() == null)) {
			for(int i=0; i<array.length; i++) {
				array[i].setOrdinal(i);
			}
		}
	}
	
	/**
	 * Helper function for setting default ordinal values, when not set.
	 * If one all the ordinal values are null, will set according to received order.
	 * @param list of objects with ordinal field to set.
	 */
	public static <E extends Ordinal> void setOrdinals(List<E> list) {
		if(list != null && list.stream().allMatch(o -> o.getOrdinal() == null)) {
			for(int i=0; i<list.size(); i++) {
				list.get(i).setOrdinal(i);
			}
		}
	}
}
