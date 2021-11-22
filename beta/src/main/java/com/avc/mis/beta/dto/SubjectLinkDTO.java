/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.SubjectLinkEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for entities who aren't data or value entities but are rather internal program structure information, 
 * either as glue between entities or instructions for business protocol.
 * Also have an ordinal value indicating priority/rank between multiple entities of the same class, 
 * owned by the same object.
 *  
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class SubjectLinkDTO extends LinkDTO implements Ordinal {

	private Integer ordinal;
	
	public SubjectLinkDTO(Integer id, Integer ordinal) {
		super(id);
		this.ordinal = ordinal;
	}
	
	public SubjectLinkDTO(SubjectLinkEntity entity) {
		super(entity);
		this.ordinal = entity.getOrdinal();
	}
	
	@Override
	public SubjectLinkEntity fillEntity(Object entity) {
		SubjectLinkEntity subjectLinkEntity;
		if(entity instanceof SubjectLinkEntity) {
			subjectLinkEntity = (SubjectLinkEntity) entity;
		}
		else {
			throw new IllegalStateException("Param has to be SubjectLinkEntity class");
		}
		super.fillEntity(subjectLinkEntity);
		subjectLinkEntity.setOrdinal(getOrdinal());
		return subjectLinkEntity;
	}
}
