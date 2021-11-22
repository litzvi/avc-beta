/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.link.ContactDetailsDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.IdCard;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.link.ContactDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying Person entity data.
 * 
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonDTO extends DataDTO {

	private String name;
	private IdCardDTO idCard;
	private ContactDetailsDTO contactDetails;
	
	public PersonDTO(Integer id, Integer version, String name) {
		super(id, version);
		this.name = name;
	}			

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
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Person.class;
	}
	
	@Override
	public Person fillEntity(Object entity) {
		Person personEntity;
		if(entity instanceof Person) {
			personEntity = (Person) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Person class");
		}
		super.fillEntity(personEntity);
		personEntity.setName(getName());
		if(getIdCard() != null) {
			personEntity.setIdCard(getIdCard().fillEntity(new IdCard()));		
		}
		if(getContactDetails() != null) {
			personEntity.setContactDetails(getContactDetails().fillEntity(new ContactDetails()));
		}
		return personEntity;
	}


}
