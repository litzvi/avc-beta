/**
 * 
 */
package com.avc.mis.beta.entities.processinfo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avc.mis.beta.entities.RankedAuditedEntity;
import com.avc.mis.beta.entities.enums.MeasureUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USED_ITEMS_BASE")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class UsedItemBase extends RankedAuditedEntity {


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storageId", updatable = false) //can't update so to not confuse the processItem of a storageMove
	private StorageBase storage;
	
	@Column(precision = 19, scale = MeasureUnit.SCALE)
	private BigDecimal numberUsedUnits;	

}
