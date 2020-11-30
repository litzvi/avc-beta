/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
public abstract class BasicSubjectDataDTO extends BasicDataDTO {

private Integer ordinal;
	
	public BasicSubjectDataDTO(Integer id, Integer version, Integer ordinal) {
		super(id, version);
		this.ordinal = ordinal;
	}
	
	public BasicSubjectDataDTO(Integer id, Integer version) {
		super(id, version);
	}
}
