/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for entities that represent process recordings who record auditing data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class ProcessDTO extends DataDTO {

	public ProcessDTO(Integer id, Integer version) {
		super(id, version);
	}
}
