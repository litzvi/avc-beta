/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public abstract class DataDTO extends BaseDTO {
	
	private Long version;
	
	public DataDTO(Integer id, Long version) {
		super(id);
		this.version = version;
	}
}
