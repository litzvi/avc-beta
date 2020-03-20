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
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@MappedSuperclass
public abstract class BaseEntityWithVersion extends BaseEntity {
	
	@Version
	private Long version;

	/**
	 * @param version
	 */
	public BaseEntityWithVersion(Integer id, Long version) {
		super(id);
		this.version = version;
	}
	
	

}
