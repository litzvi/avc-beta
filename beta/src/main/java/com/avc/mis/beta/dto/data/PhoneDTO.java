/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;

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
@NoArgsConstructor
public class PhoneDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String value;
	
	public PhoneDTO(@NonNull Phone phone) {
		this.id = phone.getId();
		this.value = phone.getValue();
	}
}
