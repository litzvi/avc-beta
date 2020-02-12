/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
				//insert the contact details
				CompanyContact.insertCompanyContact(jdbcTemplateObject, cc);
			}
		}
		
	}
}
