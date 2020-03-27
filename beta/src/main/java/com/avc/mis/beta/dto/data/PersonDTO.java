/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.data.Person;

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
public class PersonDTO extends DataDTO {

	private String name;
	private IdCardDTO idCard;
	private ContactDetailsDTO contactDetails;

	/**
	 * @param person
	 */
	public PersonDTO(@NonNull Person person) {
		super(person.getId(), person.getVersion());
		this.name = person.getName();
		if(person.getIdCard() != null)
			this.idCard = new IdCardDTO(person.getIdCard());
		if(person.getContactDetails() != null)
			this.contactDetails = new ContactDetailsDTO(person.getContactDetails());
	}

}
