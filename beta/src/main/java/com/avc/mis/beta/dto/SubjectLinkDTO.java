/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;
import com.avc.mis.beta.entities.SubjectLinkEntity;
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
public abstract class SubjectLinkDTO extends LinkDTO implements Ordinal {

	private Integer ordinal;
	
	public SubjectLinkDTO(Integer id, Integer ordinal) {
		super(id);
		this.ordinal = ordinal;
	}
	
	public SubjectLinkDTO(Integer id) {
		super(id);
	}

	public SubjectLinkDTO(SubjectLinkEntity entity) {
		super(entity);
		this.ordinal = entity.getOrdinal();
	}
	
	@JsonIgnore
	public SubjectLinkEntity fillEntity(SubjectLinkEntity entity) {
		super.fillEntity(entity);
		entity.setOrdinal(getOrdinal());
		return entity;
	}
}
