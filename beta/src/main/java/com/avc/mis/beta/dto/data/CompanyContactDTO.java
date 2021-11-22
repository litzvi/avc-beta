/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.BasicDataDTO;
import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.data.Person;
import com.avc.mis.beta.entities.values.CompanyPosition;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * DTO(Data Access Object) for sending or displaying CompanyContact entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CompanyContactDTO extends DataDTO {
	
	private BasicDataDTO<CompanyDTO> company;
	private PersonDTO person;
	private CompanyPosition position;
	
	/**
	 * @param contact CompanyContact entity
	 */
	public CompanyContactDTO(@NonNull CompanyContact contact) {
		super(contact.getId(), contact.getVersion());
//		this.company = new DataObject<Company>(contact.getCompany());
		if(contact.getPerson() == null)
			throw new NullPointerException("Company contact has to reference a person");
		this.person = new PersonDTO(contact.getPerson());
		this.position = contact.getPosition();
	}

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return CompanyContact.class;
	}
	
	@Override
	public CompanyContact fillEntity(Object entity) {
		CompanyContact contactEntity;
		if(entity instanceof CompanyContact) {
			contactEntity = (CompanyContact) entity;
		}
		else {
			throw new IllegalStateException("Param has to be CompanyContact class");
		}
		super.fillEntity(contactEntity);
		if(getCompany() != null) {
			contactEntity.setCompany((Company) getCompany().fillEntity(new Company()));
		}
		if(getPerson() != null) {
			contactEntity.setPerson(getPerson().fillEntity(new Person()));
		}
		contactEntity.setPosition(getPosition());
		return contactEntity;
	}

}
