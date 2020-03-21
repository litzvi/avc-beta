/**
 * 
 */
package com.avc.mis.beta.dto.data;

import com.avc.mis.beta.dto.BaseDTOWithVersion;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.values.CompanyPosition;

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
public class CompanyContactDTO extends BaseDTOWithVersion {

//	@EqualsAndHashCode.Exclude
//	private Integer id;
	private PersonDTO person;
	private CompanyPosition position;
	
	/**
	 * @param contact
	 */
	public CompanyContactDTO(@NonNull CompanyContact contact) {
		super(contact.getId(), contact.getVersion());		
		if(contact.getPerson() == null)
			throw new NullPointerException("Company contact has to reference a person");
		this.person = new PersonDTO(contact.getPerson());
		this.position = contact.getPosition();
	}

}
