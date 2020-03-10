/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avc.mis.beta.dataobjects.Phone;

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
