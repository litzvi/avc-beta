/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class Company {

	private Integer id;
	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	
	private ContactDetails contactDetails;
	private CompanyContact[] companyContacts;
	



	/**
	 * @param id
	 * @param name
	 * @param localName
	 * @param englishName
	 * @param license
	 * @param taxCode
	 * @param registrationLocation
	 * @param contactDetails
	 * @param companyContacts
	 */
	public Company(Integer id, String name, String localName, String englishName, String license, String taxCode,
			String registrationLocation, ContactDetails contactDetails, CompanyContact[] companyContacts) {
		super();
		this.id = id;
		this.name = name;
		this.localName = localName;
		this.englishName = englishName;
		this.license = license;
		this.taxCode = taxCode;
		this.registrationLocation = registrationLocation;
		this.contactDetails = contactDetails;
		this.companyContacts = companyContacts;
	}
	
	/**
	 * @param name
	 * @param localName
	 * @param englishName
	 * @param license
	 * @param taxCode
	 * @param registrationLocation
	 * @param contactDetails
	 * @param companyContacts
	 */
	public Company(String name, String localName, String englishName, String license, String taxCode,
			String registrationLocation, ContactDetails contactDetails, CompanyContact[] companyContacts) {
		super();
		this.name = name;
		this.localName = localName;
		this.englishName = englishName;
		this.license = license;
		this.taxCode = taxCode;
		this.registrationLocation = registrationLocation;
		this.contactDetails = contactDetails;
		this.companyContacts = companyContacts;
	}
	
	/**
	 * 
	 * @param company
	 */
	public static void insertCompany(JdbcTemplate jdbcTemplateObject, Company company) {
		
		if(company.getName() == null) {
			throw new IllegalArgumentException("Company name can't be null");
		}
		
		String sql = "INSERT INTO COMPANIES\r\n" + 
				"  (name, localName, englishName, license, taxCode, registrationLocation) \r\n" + 
				"VALUES \r\n" + 
				"  (?, ?, ?, ?, ?, ?);";		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		Object[] parameters = new Object[] {company.getName(), company.getLocalName(), company.getEnglishName(),
				company.getLicense(), company.getTaxCode(), company.getRegistrationLocation()};
		jdbcTemplateObject.update(
				new PreparedStatementCreatorImpl(sql, parameters, new String[] {"id"}), keyHolder);	
		int companyId = keyHolder.getKey().intValue();
		company.setId(companyId);		
		
		ContactDetails cd = company.getContactDetails();
		if(cd == null) {
			cd = new ContactDetails();
		}
		cd.setCompanyId(companyId);
		ContactDetails.insertContactDetails(jdbcTemplateObject, cd);
		
		CompanyContact[] companyContacts = company.getCompanyContacts();
		if(companyContacts != null) {
			for(CompanyContact cc: companyContacts) {
				cc.setCompanyId(companyId);
				CompanyContact.insertCompanyContact(jdbcTemplateObject, cc);
			}
		}
		
	}

	/**
	 * @param jdbcTemplateObject
	 */
	public void editCompany(JdbcTemplate jdbcTemplateObject) {
		if(getId() == null) {
			throw new IllegalArgumentException("Company id can't be null");
		}
		if(name != null || localName != null || englishName != null || 
				license != null || taxCode != null || registrationLocation != null) {
			// TODO update the corresponding row in companies table
		}
		if(contactDetails != null) {
			contactDetails.editContactDetails(jdbcTemplateObject);
		}
		if(companyContacts != null) {
			for(CompanyContact cc: companyContacts) {
				//search for contacts without an id - to be added
				//search for phones without a name - to be removed
				//update the given phones that have id's and names
				cc.editCompanyContact(jdbcTemplateObject);
			}
		}
	}
}
