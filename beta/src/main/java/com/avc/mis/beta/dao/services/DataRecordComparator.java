/**
 * 
 */
package com.avc.mis.beta.dao.services;

import java.util.Comparator;

import com.avc.mis.beta.dataobjects.DataRecord;

/**
 * @author Zvi
 *
 */
public class DataRecordComparator<T extends DataRecord> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		// TODO Auto-generated method stub
		return o1.getId() - o2.getId();
	}

}
