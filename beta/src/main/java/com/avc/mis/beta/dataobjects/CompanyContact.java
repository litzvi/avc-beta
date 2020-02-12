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
	
	private int companyId;
	private Position positionId;
	
	private Person person;
	
	/**
	 * @param jdbcTemplateObject
	 * @param cc
	 */
	public static void insertCompanyContact(JdbcTemplate jdbcTemplateObject, CompanyContact cc) {
		// TODO Auto-generated method stub
		
	}
	
}
