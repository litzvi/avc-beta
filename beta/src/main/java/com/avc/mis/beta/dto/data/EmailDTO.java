/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.data.Email;

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
public class EmailDTO extends SubjectDataDTO {

	private String value;
	
	public EmailDTO(@NonNull Email email) {
		super(email.getId(), email.getVersion(), email.getOrdinal());
		this.value = email.getValue();
	}
}
