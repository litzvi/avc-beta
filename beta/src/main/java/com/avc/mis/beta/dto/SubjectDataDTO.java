/**
 * 
 */
package com.avc.mis.beta.dto;

import com.avc.mis.beta.entities.Ordinal;

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

	private Integer ordinal;
	
	public SubjectDataDTO(Integer id, Integer version, Integer ordinal) {
		super(id, version);
		this.ordinal = ordinal;
	}
	
	public SubjectDataDTO(Integer id, Integer version) {
		super(id, version);
	}
}
