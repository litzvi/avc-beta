/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.ValueEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for all non entities. e.g. view, query and basic classes.
 * Not inserted by users, therefore id won't be null.
 * Will typically (also) compare ids for comparing 2 objects of the same class.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public abstract class BasicValueDTO extends BasicDTO {

	public abstract String getValue();
	
	public BasicValueDTO(Integer id) {
		super(id);
	}
	
	@Override
	public BaseEntity fillEntity(Object entity) {
		BaseEntity baseEntity;
		if(entity instanceof BaseEntity) {
			baseEntity = (BaseEntity) entity;
		}
		else {
			throw new IllegalStateException("Param has to be BaseEntity class");
		}
		super.fillEntity(baseEntity);
		return baseEntity;
	}
}
