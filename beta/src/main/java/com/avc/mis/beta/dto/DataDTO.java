/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO for entities that can be edited by multiple users.
 * contains a version.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class DataDTO extends BaseEntityDTO {
	
	@EqualsAndHashCode.Exclude
	private Integer version;
	
	public DataDTO(Integer id, Integer version) {
		super(id);
		this.version = version;
	}
}
