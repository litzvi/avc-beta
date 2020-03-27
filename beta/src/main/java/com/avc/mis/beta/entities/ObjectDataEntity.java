/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.Column;
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
public abstract class ObjectDataEntity extends DataEntity implements SoftDeleted {

	@Column(columnDefinition = "boolean default true",nullable = false)
	private boolean active = true;
}
