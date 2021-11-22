/**
 * 
 */
package com.avc.mis.beta.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Abstract class for AuditedEntity that have an order,
 * represented by an ordinal value indicating priority between multiple entities 
 * of the same class that are owned by the same object.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@MappedSuperclass
public abstract class RankedAuditedEntity extends AuditedEntity implements Ordinal {

	@Column(nullable = false)
	private Integer ordinal;

}
