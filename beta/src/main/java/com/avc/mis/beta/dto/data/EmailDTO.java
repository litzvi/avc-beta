/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.DataEntity;
import com.avc.mis.beta.entities.data.Email;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying Email entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EmailDTO extends SubjectDataDTO {

	private String value;
	
	public EmailDTO(@NonNull Email email) {
		super(email.getId(), email.getVersion(), email.getOrdinal());
		this.value = email.getValue();
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Email.class;
	}
}
