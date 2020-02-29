/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.avc.mis.beta.dataobjects.CompanyPosition;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.IdCard;
import com.avc.mis.beta.dataobjects.Person;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class PersonDTO implements Serializable {

	private Integer id;
	private String name;
	private IdCard idCard;
	private ContactDetailsDTO contactDetails;

	/**
	 * @param person
	 */
	public PersonDTO(Person person) {
		this.id = person.getId();
		this.name = person.getName();
		this.idCard = person.getIdCard();
		this.contactDetails = new ContactDetailsDTO(person.getContactDetails());
	}

}