/**
 * 
 */
package com.avc.mis.beta.dataobjects;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;

/**
 * @author Zvi
 *
 */
@Data
public class CompanyContact {
	
	private int companyId;
	private int positionId;
	
	private Person person;
	
	/**
	 * @param jdbcTemplateObject
	 * @param cc
	 */
	public static void insertCompanyContact(JdbcTemplate jdbcTemplateObject, CompanyContact cc) {
		// TODO Auto-generated method stub
		
	}
	
}
