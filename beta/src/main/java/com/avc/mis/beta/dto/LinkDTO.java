/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.LinkEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for entities who aren't data or value entities but are rather internal program structure information, 
 * either as glue between entities or instructions for business protocol.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class LinkDTO extends BaseEntityDTO {

	public LinkDTO(Integer id) {
		super(id);
	}

	
	public LinkDTO(LinkEntity entity) {
		super(entity);
	}

	@Override
	public LinkEntity fillEntity(Object entity) {
		LinkEntity linkEntity;
		if(entity instanceof LinkEntity) {
			linkEntity = (LinkEntity) entity;
		}
		else {
			throw new IllegalStateException("Param has to be LinkEntity class");
		}
		super.fillEntity(linkEntity);
		
		return linkEntity;
	}
}
