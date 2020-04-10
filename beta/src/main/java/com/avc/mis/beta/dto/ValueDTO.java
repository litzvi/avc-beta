/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO for entities who represent records that already exists in the database. e.g. country, city.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class ValueDTO extends BaseDTO {

	public ValueDTO(@NonNull Integer id) {
		super(id);
	}
}
