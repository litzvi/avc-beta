/**
 * 
 */
package com.avc.mis.beta.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO for entities who represent records that already exists in the database. e.g. country, city.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public abstract class ValueDTO extends BaseEntityDTO {

	public ValueDTO(@NonNull Integer id) {
		super(id);
	}
	
	public abstract String getValue();
}
