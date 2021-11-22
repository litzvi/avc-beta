/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for for LinkEntity that has an ordinal value,
 * indicating priority between multiple entities 
 * of the same class that are owned by the same object.
 *  
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class SubjectLinkEntity extends LinkEntity implements Ordinal {

	@Column(nullable = false)
	private Integer ordinal; 
		
}
