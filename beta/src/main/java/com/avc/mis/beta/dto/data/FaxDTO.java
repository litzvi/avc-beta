/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
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
public class FaxDTO extends SubjectDataDTO {

	private String value;
	
	public FaxDTO(@NonNull Fax fax) {
		super(fax.getId(), fax.getVersion(), fax.getOrdinal());
		this.value = fax.getValue();
	}
}
