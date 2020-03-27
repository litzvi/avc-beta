/**
 * 
 */
package com.avc.mis.beta.dto;

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
public abstract class LinkDTO extends BaseDTO {

	public LinkDTO(Integer id) {
		super(id);
	}
}
