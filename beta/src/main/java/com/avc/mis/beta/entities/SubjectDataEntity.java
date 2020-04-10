/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class representing entities that provide information of another object 
 * and not referenced by other objects.
 * Has an ordinal value indicating priority between multiple entities of the same class, owned by the same object.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class SubjectDataEntity extends DataEntity implements Ordinal{

	private Integer ordinal = -1; 
	
}
