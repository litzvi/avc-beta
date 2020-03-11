/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.Email;
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
public class EmailDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String value;
	
	public EmailDTO(@NonNull Email email) {
		this.id = email.getId();
		this.value = email.getValue();
	}
}