/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;

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
public class CompanyDTO implements Serializable {
	@EqualsAndHashCode.Exclude
	private Integer id;
	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	private ContactDetailsDTO contactDetails;
	private Set<CompanyContactDTO> companyContacts = new HashSet<>();
	
	/**
	 * @param company
	 */
	public CompanyDTO(@NonNull Company company) {
		this.id = company.getId();
		this.name = company.getName();
		this.localName = company.getLocalName();
		this.englishName = company.getEnglishName();
		this.license = company.getLicense();
		this.taxCode = company.getTaxCode();
		this.registrationLocation = company.getRegistrationLocation();
		if(company.getContactDetails() != null)
			this.contactDetails = new ContactDetailsDTO(company.getContactDetails());
		if(company.getCompanyContacts() != null)
			Arrays.stream(company.getCompanyContacts())
				.forEach((contact) -> this.companyContacts.add(new CompanyContactDTO(contact)));
	}
	
	public void addCompanyContact(@NonNull CompanyContact contact) {
		this.companyContacts.add(new CompanyContactDTO(contact));
	}
	
}
