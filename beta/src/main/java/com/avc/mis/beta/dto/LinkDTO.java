/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for entities who aren't data or value entities but are rather internal program structure information, 
 * either as glue between entities or instructions for business protocol.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class LinkDTO extends BaseEntityDTO {

	public LinkDTO(Integer id) {
		super(id);
	}
}
