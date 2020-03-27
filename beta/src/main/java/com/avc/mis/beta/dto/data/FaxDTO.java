/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.data.Fax;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class FaxDTO extends DataDTO {

	private String value;
	
	public FaxDTO(@NonNull Fax fax) {
		super(fax.getId(), fax.getVersion());
		this.value = fax.getValue();
	}
}
