/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.MappedSuperclass;

/**
 * Base class extended by all persistence entities.
 * Checks Legality of persisted or updated entity before updating database.
 * 
 * @author Zvi
 *
 */
@MappedSuperclass
public abstract class BaseEntity implements Insertable {

	//batch size used for entities
//	public static final int BATCH_SIZE = 20;
			
//	public abstract String getIllegalMessage();
//	
//	@PrePersist
//	@Override
//	public void prePersist() {
//		if(!isLegal())
//			throw new IllegalArgumentException(this.getIllegalMessage());
//	}
//	
//	@PreUpdate
//	@Override
//	public void preUpdate() {
//		if(!isLegal())
//			throw new IllegalArgumentException(this.getIllegalMessage());
//	}
}
