/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
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
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PhoneDTO extends DataDTO {

	private String value;
	
	public PhoneDTO(@NonNull Phone phone) {
		super(phone.getId(), phone.getVersion());
		this.value = phone.getValue();
	}
}
