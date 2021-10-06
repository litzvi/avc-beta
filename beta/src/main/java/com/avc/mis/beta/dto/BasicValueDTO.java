/**
 * 
 */
package com.avc.mis.beta.dto;

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
	
	@JsonIgnore
	public ValueEntity fillEntity(ValueEntity entity) {
		super.fillEntity(entity);
		return entity;
	}
}
