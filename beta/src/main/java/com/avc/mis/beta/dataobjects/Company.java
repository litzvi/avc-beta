/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.avc.mis.beta.dao.services.PreparedStatementCreatorImpl;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class Company {

	private int id;
	private String name;
	private String localName;
	private String englishName;
	private String license;
	private String taxCode;
	private String registrationLocation;
	
	private ContactDetails contactDetails;
	private CompanyContact[] companyContacts;
	
	
	/**
	 * 
	 * @param company
	 */
	public static void insertCompany(JdbcTemplate jdbcTemplateObject, Company company) {
		
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
		if(cd != null) {
			cd.setCompanyId(companyId);
			ContactDetails.insertContactDetails(jdbcTemplateObject, cd);
		}	
		
		CompanyContact[] companyContacts = company.getCompanyContacts();
		if(companyContacts != null) {
			for(CompanyContact cc: companyContacts) {
				cc.setCompanyId(companyId);
				//insert the contact details
			}
		}
		
	}
}
