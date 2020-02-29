/**
 * 
 */
package com.avc.mis.beta.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.avc.mis.beta.dataobjects.Company;
import com.avc.mis.beta.dataobjects.CompanyContact;
import com.avc.mis.beta.dataobjects.ContactDetails;
import com.avc.mis.beta.dataobjects.Supplier;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class CompanyDTO implements Serializable {

	private Integer id;
	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private ContactDetailsDTO contactDetails;
	private Set<CompanyContactDTO> companyContacts = new HashSet<>();
	
	/**
	 * @param company
	 */
	public CompanyDTO(Company company) {
		this.id = company.getId();
		this.name = company.getName();
		this.localName = company.getLocalName();
		this.englishName = company.getEnglishName();
		this.license = company.getLicense();
		this.taxCode = company.getTaxCode();
//		this.contactDetails = new ContactDetailsDTO(company.getContactDetails());
//		company.getCompanyContacts().forEach((contact) -> this.companyContacts.add(new CompanyContactDTO(contact)));
	}
	
	public void addCompanyContact(CompanyContact contact) {
		this.companyContacts.add(new CompanyContactDTO(contact));
	}
	
	public void setContactDetails(ContactDetails contactDetails) {
		new ContactDetailsDTO(contactDetails);
	}

}
