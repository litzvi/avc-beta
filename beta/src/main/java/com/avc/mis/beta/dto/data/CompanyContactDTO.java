/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;

import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.CompanyPosition;

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
public class CompanyContactDTO implements Serializable {

	@EqualsAndHashCode.Exclude
	private Integer id;
	private PersonDTO person;
	private CompanyPosition position;
	
	/**
	 * @param contact
	 */
	public CompanyContactDTO(@NonNull CompanyContact contact) {
		this.id = contact.getId();
		if(contact.getPerson() == null)
			throw new NullPointerException("Company contact has to reference a person");
		this.person = new PersonDTO(contact.getPerson());
		this.position = contact.getPosition();
	}

}
