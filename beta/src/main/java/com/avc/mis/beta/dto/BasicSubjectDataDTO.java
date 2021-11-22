/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for non entities. e.g. view, query and reports, that are ordered.
 * Contain an ordinal to determine the order of objects within a collection.
 * Not inserted by users, therefore id won't be null.
 * Will typically (also) compare ids for comparing 2 objects of the same class.
 * 
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
public abstract class BasicSubjectDataDTO extends BasicDataValueDTO {

	private Integer ordinal;
	
	public BasicSubjectDataDTO(Integer id, Integer version, Integer ordinal) {
		super(id, version);
		this.ordinal = ordinal;
	}
}
