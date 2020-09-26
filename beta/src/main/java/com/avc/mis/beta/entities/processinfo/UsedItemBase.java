/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;

import com.avc.mis.beta.entities.AuditedEntity;
import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.embeddable.AmountWithUnit;

/**
 * @author zvi
 *
 */
@MappedSuperclass
public abstract class UsedItemBase extends AuditedEntity {

	public abstract Storage getStorage();
	
	public abstract BigDecimal getUsedUnits();
}
