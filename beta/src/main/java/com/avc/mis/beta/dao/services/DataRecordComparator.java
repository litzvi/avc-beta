/**
 * 
 */
package com.avc.mis.beta.dao.services;

import java.util.Comparator;

import com.avc.mis.beta.entities.Insertable;

/**
 * @author Zvi
 *
 */
public class DataRecordComparator<T extends Insertable> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		return o1.getId() - o2.getId();
	}
}
