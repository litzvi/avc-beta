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
	
}
