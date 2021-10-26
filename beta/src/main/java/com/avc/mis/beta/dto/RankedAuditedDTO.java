/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.RankedAuditedEntity;
import com.avc.mis.beta.entities.SubjectDataEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class RankedAuditedDTO extends DataDTO implements Ordinal {

	@EqualsAndHashCode.Exclude
	private Integer ordinal;
	
	public RankedAuditedDTO(Integer id, Integer version, Integer ordinal) {
		super(id, version);
		this.ordinal = ordinal;
	}
	
	public RankedAuditedDTO(Integer id, Integer version) {
		super(id, version);
	}
	
	public RankedAuditedDTO(RankedAuditedEntity entity) {
		super(entity);
		this.ordinal = entity.getOrdinal();
	}
	
	@JsonIgnore
	@Override
	public RankedAuditedEntity fillEntity(Object entity) {
		RankedAuditedEntity rankedAuditedEntity;
		if(entity instanceof RankedAuditedEntity) {
			rankedAuditedEntity = (RankedAuditedEntity) entity;
		}
		else {
			throw new IllegalArgumentException("Param has to be RankedAuditedEntity class");
		}
		super.fillEntity(rankedAuditedEntity);
		rankedAuditedEntity.setOrdinal(getOrdinal());
		return rankedAuditedEntity;
	}
	
}
