/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class BasicDataDTO extends BasicValueDTO {

	private Integer version;
	
	public BasicDataDTO(Integer id, Integer version) {
		super(id);
		this.version = version;
	}
}
