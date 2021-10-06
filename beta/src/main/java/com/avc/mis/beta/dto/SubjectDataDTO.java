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
 * DTO for entities that represent information of another object 
 * and not referenced by other objects.
 * Also have an ordinal value indicating priority between multiple entities of the same class, 
 * owned by the same object.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class SubjectDataDTO extends DataDTO implements Ordinal {

	@EqualsAndHashCode.Exclude
	private Integer ordinal;
	
	public SubjectDataDTO(Integer id, Integer version, Integer ordinal) {
		super(id, version);
		this.ordinal = ordinal;
	}
	
	public SubjectDataDTO(Integer id, Integer version) {
		super(id, version);
	}
	
	public SubjectDataDTO(RankedAuditedEntity entity) {
		super(entity);
		this.ordinal = entity.getOrdinal();
	}
	
	@JsonIgnore
	public SubjectDataEntity fillEntity(SubjectDataEntity entity) {
		super.fillEntity(entity);
		entity.setOrdinal(getOrdinal());
		return entity;
	}
	
//	@JsonIgnore
//	public SubjectLinkEntity fillEntity(SubjectLinkEntity entity) {
//		super.fillEntity(entity);
//		entity.setOrdinal(getOrdinal());
//		return entity;
//	}
//	
	@JsonIgnore
	public RankedAuditedEntity fillEntity(RankedAuditedEntity entity) {
		super.fillEntity(entity);
		entity.setOrdinal(getOrdinal());
		return entity;
	}
}
