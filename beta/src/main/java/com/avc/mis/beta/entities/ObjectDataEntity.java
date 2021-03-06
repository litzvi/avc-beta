/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class representing entities that represent a real world interacting object. 
 * Usually referenced by other data entities, therefore should only be soft deleted. 
 * Contains a flag to set as not active - soft deleted.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class ObjectDataEntity extends DataEntity implements SoftDeleted {

	@Column(nullable = false, updatable = false, columnDefinition = "boolean default true")
	private boolean active = true;
	
}
