/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avc.mis.beta.dataobjects.CompanyContact;
import com.avc.mis.beta.dataobjects.CompanyPosition;
import com.avc.mis.beta.dataobjects.Person;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class CompanyContactDTO implements Serializable {

	private PersonDTO person;
	private CompanyPosition position;
	
	/**
	 * @param contact
	 */
	public CompanyContactDTO(@NonNull CompanyContact contact) {
		if(contact.getPerson() == null)
			throw new NullPointerException("Company contact has to reference a person");
		this.person = new PersonDTO(contact.getPerson());
		this.position = contact.getPosition();
	}

}
