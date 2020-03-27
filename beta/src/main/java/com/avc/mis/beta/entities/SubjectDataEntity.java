/**
 * 
 */
package com.avc.mis.beta.entities;

import java.util.Comparator;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class SubjectDataEntity extends DataEntity {

	private int ordinal; 
	
	public static Comparator<SubjectDataEntity> ordinalComparator() {
		return new Comparator<SubjectDataEntity>() {

			@Override
			public int compare(SubjectDataEntity o1, SubjectDataEntity o2) {
				return o1.getOrdinal()-o2.getOrdinal();
			}};
		
	}
	
	public static void setOrdinals(SubjectDataEntity[] array) {
		for(int i=0; i<array.length; i++) {
			array[i].setOrdinal(i);
		}
	}
}
