/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @author Zvi
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements Insertable {
	
	public abstract String getIllegalMessage();
	
	@PrePersist @PreUpdate
	@Override
	public void prePersistOrUpdate() {
		if(!isLegal())
			throw new IllegalArgumentException(this.getIllegalMessage());
	}
}
