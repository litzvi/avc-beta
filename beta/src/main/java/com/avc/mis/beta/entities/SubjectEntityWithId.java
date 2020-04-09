/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zvi
 *
 * Abstract class representing entities with ID, that provide information of another object 
 * and not referenced by other objects.
 * Has an ordinal value indicating priority between multiple entities of the same class, owned by the same object.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MappedSuperclass
public abstract class SubjectEntityWithId extends SubjectDataEntity {

	@EqualsAndHashCode.Include
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
}
