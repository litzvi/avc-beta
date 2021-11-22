/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.RankedAuditedEntity;
import com.avc.mis.beta.entities.SubjectDataEntity;

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
	
	public SubjectDataDTO(RankedAuditedEntity entity) {
		super(entity);
		this.ordinal = entity.getOrdinal();
	}
	
	@Override
	public SubjectDataEntity fillEntity(Object entity) {
		SubjectDataEntity subjectDataEntity;
		if(entity instanceof SubjectDataEntity) {
			subjectDataEntity = (SubjectDataEntity) entity;
		}
		else {
			throw new IllegalStateException("Param has to be SubjectDataEntity class");
		}
		super.fillEntity(subjectDataEntity);
		subjectDataEntity.setOrdinal(getOrdinal());
		return subjectDataEntity;
	}
	
}
