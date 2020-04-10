/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Abstract class representing an entity usually already exists in the database. e.g. country, city.
 * Typically referenced by user data but only references other value entities.
 * Referenced by user data, therefore should only be soft deleted but not physically deleted.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MappedSuperclass
public abstract class ValueEntity extends BaseEntity implements SoftDeleted {
	
	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "boolean default true")
	private boolean active;

}
