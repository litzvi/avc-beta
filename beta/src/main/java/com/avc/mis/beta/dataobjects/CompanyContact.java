/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
public class CompanyContact {
	
	private Integer companyId;
	private Position position;
	
	private Person person;
	
	/**
	 * @param jdbcTemplateObject
	 * @param cc
	 */
	public static void insertCompanyContact(JdbcTemplate jdbcTemplateObject, CompanyContact cc) {
		if(cc.getCompanyId() != null && cc.getPerson() != null) {
			Person.insertPerson(jdbcTemplateObject, cc.getPerson());
			Integer personId = cc.getPerson().getId();
			if(personId != null) {
				Integer positionId = cc.getPosition() != null ? cc.getPosition().getId(): null;
				String sql = "INSERT INTO COMPANY_CONTACTS (personId, companyId, positionId) VALUES (?, ?, ?)";
				jdbcTemplateObject.update(sql, new Object[] {personId, cc.getCompanyId(), positionId});
			}
		}
		
	}

	/**
	 * @param jdbcTemplateObject
	 */
	public void editCompanyContact(JdbcTemplate jdbcTemplateObject) {
		if(getCompanyId() == null) {
			throw new IllegalArgumentException("Company id can't be null");
		}
		if(getPerson() == null && getPerson().getId() == null) {
			throw new IllegalArgumentException("Contact person id can't be null");
		}
		// TODO if removed change to isActive=false
		//if position changed, delete current position and add new one
		//if person changed call edit on person.
		// perhaps should add a personId, in case the person isn't changed
		
		
	}
	
}
