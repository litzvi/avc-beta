/**
 * 
 */
package com.avc.mis.beta.dto.data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.avc.mis.beta.dto.DataDTO;
import com.avc.mis.beta.dto.link.ContactDetailsDTO;
import com.avc.mis.beta.entities.BaseEntity;
import com.avc.mis.beta.entities.data.Company;
import com.avc.mis.beta.entities.data.CompanyContact;
import com.avc.mis.beta.entities.link.ContactDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * DTO(Data Access Object) for sending or displaying Company entity data.
 * 
 * @author Zvi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class CompanyDTO extends DataDTO {

	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	private ContactDetailsDTO contactDetails;
	private Set<CompanyContactDTO> companyContacts = new HashSet<>();
	
	/**
	 * @param company Company entity object
	 */
	public CompanyDTO(@NonNull Company company, boolean hasContacts) {
		super(company.getId(), company.getVersion());
		this.name = company.getName();
		this.localName = company.getLocalName();
		this.englishName = company.getEnglishName();
		this.license = company.getLicense();
		this.taxCode = company.getTaxCode();
		this.registrationLocation = company.getRegistrationLocation();
		if(company.getContactDetails() != null)
			this.contactDetails = new ContactDetailsDTO(company.getContactDetails());
		
		/*
		 * causing chaotic load of company contacts when getting from db 
		 * but needed for creating DTO from detached object
		 */
		if(hasContacts && company.getCompanyContacts() != null)
			company.getCompanyContacts().stream()
				.forEach((contact) -> this.companyContacts.add(new CompanyContactDTO(contact)));
	}
	
	/**
	 * Adds a company contact - without altering already added contacts.
	 * @param contact CompanyContact entity object
	 */
	public void addCompanyContact(@NonNull CompanyContact contact) {
		this.companyContacts.add(new CompanyContactDTO(contact));
	}
	
	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		return Company.class;
	}
	
	@Override
	public Company fillEntity(Object entity) {
		Company companyEntity;
		if(entity instanceof Company) {
			companyEntity = (Company) entity;
		}
		else {
			throw new IllegalStateException("Param has to be Company class");
		}
		super.fillEntity(companyEntity);
		companyEntity.setName(getName());
		companyEntity.setLocalName(getLocalName());
		companyEntity.setEnglishName(getEnglishName());
		companyEntity.setLicense(getLicense());
		companyEntity.setTaxCode(getTaxCode());
		companyEntity.setRegistrationLocation(getRegistrationLocation());
		if(getContactDetails() != null) {
			companyEntity.setContactDetails(getContactDetails().fillEntity(new ContactDetails()));
		}
		if(getCompanyContacts() != null) {
			companyEntity.setCompanyContacts(getCompanyContacts().stream().map(i -> i.fillEntity(new CompanyContact())).collect(Collectors.toSet()));
		}
		
		return companyEntity;
	}

	
}
