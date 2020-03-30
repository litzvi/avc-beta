/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class SubjectDataDTO extends DataDTO implements Ordinal {

	private Integer ordinal;
	
	public SubjectDataDTO(Integer id, Long version, Integer ordinal) {
		super(id, version);
		this.ordinal = ordinal;
	}
}
