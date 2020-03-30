/**
 * 
 */
package com.avc.mis.beta.entities;

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
public abstract class SubjectDataEntity extends DataEntity implements Ordinal{

	private Integer ordinal = -1; 
	
}
