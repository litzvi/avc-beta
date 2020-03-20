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
public abstract class BaseDTOWithVersion extends BaseDTO {
	
	private Integer id;
	private Long version;
}
