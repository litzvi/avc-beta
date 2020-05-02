/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.SubjectDataDTO;
import com.avc.mis.beta.entities.data.Phone;

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
public class PhoneDTO extends SubjectDataDTO {

	private String value;
	
	public PhoneDTO(@NonNull Phone phone) {
		super(phone.getId(), phone.getVersion(), phone.getOrdinal());
		this.value = phone.getValue();
	}

	/**
	 * @param id
	 * @param version
	 * @param value
	 */
	public PhoneDTO(Integer id, Integer version, String value, int ordinal) {
		super(id, version, ordinal);
		this.value = value;
	}
}
