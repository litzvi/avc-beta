/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntityNoIdWithVersion extends BaseEntityNoId {
	@Version
	private Long version;
}
