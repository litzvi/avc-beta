/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class for entities that represent data that can be seen and updated by more than one user,
 * therefore needs to have a version.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class DataEntity extends BaseEntity {
	@Version
	private Long version;
}
