/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for all non entities. e.g. view and query classes.
 * 
 * @author zvi
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class DTOWithId {

	@EqualsAndHashCode.Include
	private Integer id;

}
